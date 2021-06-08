package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.service.IOrderService;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 1:07
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private IOrderService orderService;
    private IUserService userService;

    @Autowired
    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/query")
    public ServerResponse<List<VOOrder>> getOrder(@RequestParam String phone) {
        try{
            logger.info("[用户查询订单]： 正在查询中");
            List<VOOrder> orderList = orderService.getOrderList(phone);
            logger.info("[用户查询订单]：成功查询订单");
            return ServerResponse.createBySuccessMsgData("成功查询订单", orderList);
        }catch (Exception e){
            logger.error("[用户查询订单]：/user/getOrder 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }


    /**
     * 退款订单
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reverse")
    public ServerResponse<Boolean> reverseOrder(@RequestBody Order order) {
        try{
            if(orderService.reverseOrder(order.getOrderId())){
                logger.info("[售票员退票]：退票成功");
                return ServerResponse.createBySuccessMsgData("退票成功", true);
            }
            logger.info("[售票员退票]: 退票失败");
            return ServerResponse.createBySuccessMsgData("退票失败", false);
        }catch (Exception e){
            logger.error("[售票员退票]：/user/reverse 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    /**
     * 售票员自助充值
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/addBalance")
    public ServerResponse<Boolean> addBalance(@RequestBody User user) {
        try{
            if(user.getPhone() == null || user.getPhone().length() != 11){
                throw new MyException("参数不合法");
            }
            int id = userService.logIn(user.getPhone());
            if(userService.updateUserMoney(id, user.getBalance())){
                logger.info("[售票员现场充值]：充值成功");
                return ServerResponse.createBySuccessMsgData("充值成功", true);
            }
            logger.info("[售票员现场充值]: 充值失败");
            return ServerResponse.createBySuccessMsgData("充值失败", false);
        }catch (MyException e){
            logger.error(e.getMessage());
            return ServerResponse.createByErrorMsgData(e.getMessage(), false);
        }catch (Exception e){
            logger.error("[售票员现场充值]：/order/addBalance 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }
}
