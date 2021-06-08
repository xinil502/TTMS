package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.IOrderMapper;
import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.service.IOrderService;
import cn.xinill.ttms.service.ITicketService;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @Author: Xinil
 * @Date: 2021/6/7 23:08
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private IOrderMapper orderMapper;
    private ITicketService ticketService;
    private IUserService userService;

    @Autowired(required = false)
    public void setOrderMapper(IOrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setTicketService(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public long addOrder(Order order) {
        order.setOrderId(order.getPayTime()*2);
        if(1 == orderMapper.addOrder(order)){
            return order.getOrderId();
        }else{
            return -1;
        }
    }

    @Override
    public List<VOOrder> getOrderList(int userId) {
        List<VOOrder> voOrders = orderMapper.selectOrderByUserId(userId);
        long time = System.currentTimeMillis();
        for(VOOrder order: voOrders){
            order.setTickets(ticketService.findTicketListByOrderId(order.getOrderId()));
            if(order.getOrderExistTime() < time){
                order.setOrderStatus(0);
            }
        }
        return voOrders;
    }

    @Override
    public List<VOOrder> getOrderList(String phone) {
        //获取用户id
        int userId = userService.logIn(phone);
        return getOrderList(userId);
    }

    @Override
    public Boolean reverseOrder(Long orderId) throws MyException{
        //判断订单状态更新
        Order order = orderMapper.selectUserIdByOrderId(orderId);
        if(order == null || order.getStatus()!=1 || order.getOrderExistTime() < System.currentTimeMillis()){
            throw new MyException("订单已失效，无法退款");
        }else{
            //更改影票状态
            ticketService.reverseTicket(orderId);
            //更改订单变更时间和订单状态
            order.setPayTime(System.currentTimeMillis());
            order.setStatus(-1);
            if(1 == orderMapper.updateStatus(order)){
                //退款
                userService.updateUserMoney(order.getUserId(), order.getPrice());
                return true;
            }else{
                throw new MyException("订单更改状态失败，无法退款");
            }
        }
    }
}
