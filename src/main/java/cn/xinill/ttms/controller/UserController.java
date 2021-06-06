package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.service.ITicketService;
import cn.xinill.ttms.vo.VOLogin;
import cn.xinill.ttms.service.ICodeService;
import cn.xinill.ttms.service.ITokenService;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.service.impl.TokenServiceImpl;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.utils.OSSClientUtil;

import cn.xinill.ttms.vo.VOSaleTicket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
@RequestMapping(value = "/user", produces="application/json;charset=UTF-8")
public class UserController {

    private final int rememberMe = 7*24*60*60;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private OSSClientUtil ossClientUtil = new OSSClientUtil();
    private IUserService userService;
    private ITokenService tokenService;
    private ICodeService codeService;
    private ITicketService ticketService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setCodeService(ICodeService codeService) {
        this.codeService = codeService;
    }

    @Autowired
    public void setTicketService(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * 用户登陆-发送验证码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public ServerResponse<Boolean> testToken(ServletRequest request) {
        return ServerResponse.createBySuccessMsgData("已登录", true);
    }
    /**
     * 用户登陆-发送验证码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login/getCode")
    public ServerResponse<Boolean> logInGetCode(@RequestBody User user) {
        //手机号合法性验证
        String phone = user.getPhone();
        logger.info("[登陆发送验证码]：phone = " + phone);
        if(phone == null || phone.length() != 11){
            logger.warn("[登陆发送验证码]：手机号长度不合法 phone = " + phone);
            return ServerResponse.createByErrorCodeMsgData(2, "手机号长度不合法", false);
        }

        try {
            //发送验证码
            codeService.sendLOGIN(phone);
            logger.info("[登陆发送验证码]：验证码发送成功");
            return ServerResponse.createBySuccessMsgData("验证码已发送", true);
        }catch (Exception e) {
            logger.error("[登陆发送验证码]：/user/login/getCode 接口错误");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 用户登陆-验证验证码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login/judgeCode")
    public ServerResponse<Boolean> logInJudgeCode(@RequestBody VOLogin login,
                                                  HttpServletResponse response) {
        String phone = login.getPhone();
        String code = login.getCode();


        logger.info("[登陆验证验证码]：phone="+phone+"code="+code);
        //参数认证
        if(phone == null || code == null){
            logger.warn("[登陆验证验证码]：参数不合法");
            return ServerResponse.createByErrorCodeMsgData(2, "参数不合法", false);
        }

        //手机号合法性验证
        if(phone.length() != 11){
            logger.warn("[登陆验证验证码]：手机号不合法");
            return ServerResponse.createByErrorCodeMsgData(2, "手机号长度不合法", false);
        }

        //验证并登陆
        try {
            if(codeService.judgeLOGIN(phone, code)){
                //获取uid，添加token。
                int userId = userService.logIn(phone);
                response.addHeader("token", tokenService.creatUserToken(userId, 6, rememberMe));//获取uid
                logger.info("[登陆验证验证码]：登陆成功，userID = " + userId);
                return ServerResponse.createBySuccessMsgData("登陆成功", true);
            }else {
                logger.warn("[登陆验证验证码]：手机号："+phone+" 验证码不正确");
                return ServerResponse.createByErrorMsgData("验证码不正确", false);
            }
        }catch (Exception e) {
            logger.error("[登陆验证验证码]： /user/login/judgeCode 接口错误");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 查看用户信息
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/get")
    public ServerResponse<User> getUserMessage(ServletRequest request){
        try {
            //处理token验证
            int id = (Integer)request.getAttribute("id");
            logger.info("[查询个人信息] userID = " + id);

            //查询用户信息
            User user = userService.findUserByUid(id);
            if(user!=null){
                logger.info("[查询个人信息]： 查询成功 "+user.toString());
                return ServerResponse.createBySuccessData(user);
            }else{
                throw new MyException("无该用户信息");
            }
        } catch (Exception e) {
            logger.error("[查询个人信息]： /user/inform/get 接口错误");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }


    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/updatePortrait")
    public ServerResponse<Boolean> updatePortrait(HttpServletRequest request,
                                                  @RequestParam MultipartFile file){
        try {
            logger.info("[用户上传头像]");
            int id = (Integer) request.getAttribute("id");
            if(userService.updatePortrait(id, file)){
                throw new MyException("用户不存在");
            }
            logger.info("[用户上传头像]：上传成功");
            return ServerResponse.createBySuccessMsgData("上传头像成功", true);
        } catch (MyException e) {
            logger.error(e.getMessage());
            return ServerResponse.createByErrorMsgData(e.getMessage(), false);
        }catch (Exception e){
            logger.error("[用户上传头像]：/user/inform/updatePortrait 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/update")
    public ServerResponse<Boolean> updateInform(ServletRequest request,
                                                @RequestBody User user) {

        int uid = (Integer)request.getAttribute("id");
        user.setUserId(uid);

        logger.info("[修改用户信息]"+user);
        try{
            if(userService.updateUserInform(user)){
                System.out.println("[修改用户信息] : 信息修改成功");
                return ServerResponse.createBySuccessMsgData("信息修改成功",true);
            }
            logger.warn("[修改用户信息]: 信息修改失败");
            return ServerResponse.createByErrorMsgData("修改信息失败", false);
        }catch (Exception e){
            logger.error("[修改用户信息]：/user/inform/update 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }
    /**
     * 用户购票
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/buyTickets")
    public ServerResponse<Boolean> updateInform(ServletRequest request,
                                                @RequestBody VOSaleTicket saleTicket) {
        try{
            int id = (Integer)request.getAttribute("id");

            if(saleTicket.getTickets().length == 0 || saleTicket.getTime() == null) throw new MyException("参数不合法");
            logger.info("[用户购票]： scheduleId = {userId = " +id+ Arrays.toString(saleTicket.getTickets()) + saleTicket.getTime());
            ticketService.saleTickets(saleTicket, id);
            logger.info("[用户购票]：成功购买影票");
            return ServerResponse.createBySuccessMsgData("成功购买影票", true);
        } catch (MyException e) {
            logger.error("[用户购票]：" + e.getMessage());
            return ServerResponse.createByErrorMsgData(e.getMessage(), false);
        }catch (Exception e){
            logger.error("[用户购票]：/user/inform/byTickets 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }
}
