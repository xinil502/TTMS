package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.vo.VOLogin;
import cn.xinill.ttms.service.ICodeService;
import cn.xinill.ttms.service.ITokenService;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.service.impl.TokenServiceImpl;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.utils.OSSClientUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
@RequestMapping(value = "/user", produces="application/json;charset=UTF-8")
public class UserController {

    private final int rememberMe = 7*24*60*60;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private OSSClientUtil ossClientUtil = new OSSClientUtil();
    private IUserService userService;
    private ITokenService tokenService;
    private ICodeService codeService;

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

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET , value = "/test")
    public ServerResponse<String> test(HttpServletRequest request){
        int uid = -1;
        try {
            String token = request.getHeader("token");
            if(token == null){
                System.err.println("token 为null");
                return ServerResponse.createBySuccessMsg("需要登陆");
            }
            uid = new TokenServiceImpl().verifyUserToken(token);
            if(uid == -1){
                System.err.println("登陆已过期");
                return ServerResponse.createBySuccessMsg("需要登陆");
            }
            System.out.println("获取token uid=" + uid);
            return ServerResponse.createBySuccessMsgData("测试登陆成功", "userId = " + uid);

        } catch (Exception e) {//需要登陆
            e.printStackTrace();
            System.err.println("需要登陆");
            return ServerResponse.createBySuccessMsg("需要登陆");
        }
    }

    /**
     * 用户手机号密码登陆
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST , value = "/login/password")
    public ServerResponse<Boolean> logIn(@RequestParam String phone,
                                         @RequestParam String password,
                                         HttpServletResponse response){
        try {
            int uid = userService.logIn(phone, password);
            if(uid == -1){
                System.out.println("[id:"+uid+"] 账号密码登陆失败");
                return ServerResponse.createBySuccessMsgData("密码错误", false);
            }else{
                System.out.println("[id:"+uid+"] 账号密码登陆成功");
                response.addHeader("token", tokenService.creatUserToken(uid, rememberMe));
                return ServerResponse.createBySuccessMsgData("密码正确", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
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
                //未注册用户进行注册
                if (!userService.phoneExist(phone)){
                    userService.logOn(phone);
                }

                //获取uid，添加token。
                int userId = userService.logIn(phone);
                response.addHeader("token", tokenService.creatUserToken(userId, rememberMe));//获取uid
                logger.info("[登陆验证验证码]：登陆成功，userID = " + userId);
                return ServerResponse.createBySuccessData(true);
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
     * 重要信息修改-发送验证码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/importantChange/getCode")
    public ServerResponse<Boolean> importantInformGetCode(ServletRequest request) {
        try {
            //处理token验证
            int uid = (Integer)request.getAttribute("uid");
            if(uid == -1){
                return ServerResponse.createByErrorCodeMsgData(10, "需要token登陆", false);
            }

            //查询用户信息
            User user = userService.findUserByUid(uid);
            //发送验证码
            codeService.sendUPDATE(user.getPhone());
            return ServerResponse.createBySuccessMsgData("验证码已发送", true);
        }catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 重要信息修改-检验验证码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/importantChange/judgeCode")
    public ServerResponse<Boolean> importantInformJudgeCode(@RequestParam String code,
                                                            ServletRequest request) {
        try {
            //处理token验证
            int uid = (Integer)request.getAttribute("uid");
            if(uid == -1){
                return ServerResponse.createByErrorCodeMsgData(10, "需要token登陆", false);
            }

            User user = userService.findUserByUid(uid);
            //查询用户信息
            if(user!=null && codeService.judgeUPDATE(user.getPhone(), code)){ //验证码验证成功
                return ServerResponse.createBySuccessData(true);
            }else{
                return ServerResponse.createByErrorMsgData("验证码不正确", false);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 修改用户密码
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/updatepwd")
    public ServerResponse<Boolean> updatePwd(@RequestParam String password, ServletRequest request) {
        try {
            //处理token验证
            int uid = (Integer)request.getAttribute("uid");
            if(uid == -1){
                return ServerResponse.createByErrorCodeMsgData(10, "需要token登陆", false);
            }

            //处理密码长度
            if(password.length() < 6){
                return ServerResponse.createByErrorMsgData("用户密码长度不得少于6", false);
            }else if(password.length() >20){
                return ServerResponse.createByErrorMsgData("用户密码长度不得大于20", false);
            }

            //修改密码
            System.out.println("[id:"+uid+"] 修改密码为"+password);
            if(userService.updateUserPwd(uid, password)){
                return ServerResponse.createBySuccessMsgData("密码修改成功", true);
            }
            System.err.println("[id:"+uid+"] 修改密码失败");
            return ServerResponse.createByErrorMsgData("修改密码失败", false);
        } catch (Exception e) {
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
            int uid = (Integer)request.getAttribute("uid");
            if(uid == -1){
                return ServerResponse.createByErrorCodeMsgData(10, "需要token登陆", null);
            }
            System.out.println("[id:"+uid+"] 查询个人信息");

            //查询用户信息
            User user = userService.findUserByUid(uid);
            if(user!=null){
                user.setPassword(null);
                user.setPortrait("https://xinil.oss-cn-shanghai.aliyuncs.com/smart_photo/"+user.getPortrait());
                return ServerResponse.createBySuccessData(user);
            }else{
                return ServerResponse.createByErrorMsg("无该用户信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMsg("服务器异常，请赶快通知任!!!");
        }
    }


    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform/update")
    public ServerResponse<Boolean> updateInform(ServletRequest request,
                                                String username,
                                                MultipartFile portrait,
                                                Integer age,
                                                String gender,
                                                //String email,
                                                String introduce) {


        //处理token验证
        int uid = (Integer)request.getAttribute("uid");
        if(uid == -1){
            return ServerResponse.createByErrorCodeMsgData(10, "需要token登陆", false);
        }
        System.out.println("[id:"+uid+"] ——————————修改个人信息——————————");

        //处理昵称
        if (username != null) {
            if(username.length() < 1){
                System.err.println("[id:"+uid+"]  昵称过短");
                return ServerResponse.createByErrorCodeMsgData(2,"昵称过短", false);
            }else if(username.length() > 20){
                System.err.println("[id:"+uid+"]  昵称长度不能超过20字符");
                return ServerResponse.createByErrorCodeMsgData(2,"昵称长度不能超过20字符", false);
            }
            System.out.println("[id:"+uid+"] 修改昵称："+username);
        }

        //处理头像
        String url = null;
        if (portrait != null) {
            try {
                url =  ossClientUtil.uploadImg2Oss(portrait);
            } catch (MyException e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMsgData("无法更新头像", false);
            }
            System.out.println("[id:"+uid+"] 修改头像："+"https://xinil.oss-cn-shanghai.aliyuncs.com/smart_photo/"+portrait);
        }

        //处理性别
        if (gender != null) {
            if(gender.length() > 1){
                System.err.println("[id:"+uid+"] 性别格式不正确");
                return ServerResponse.createByErrorCodeMsgData(2,"性别格式不对", false);
            }
            System.out.println("[id:"+uid+"] 修改性别："+gender);
        }

        //处理个人简介
        if(introduce != null) {
            System.out.println("[id:"+uid+"] 修改简介："+introduce);
            if(introduce.length() > 255){
                System.err.println("[id:"+uid+"] 个人简介信息超过255字符");
                return ServerResponse.createByErrorMsgData("个人简介信息最长为255字符", false);
            }
        }


        try{
            System.out.println("[id:"+uid+"] ——————————信息处理完毕——————————");
            if(userService.updateUserInform(uid, username, url, gender, age, introduce)){
                System.out.println("[id:"+uid+"] 信息修改成功");
                return ServerResponse.createBySuccessData(true);
            }
            System.out.println("[id:"+uid+"] 信息修改失败");
            return ServerResponse.createByErrorMsg("修改信息失败");
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMsg("服务器异常，请赶快通知任!!!");
        }
    }
}
