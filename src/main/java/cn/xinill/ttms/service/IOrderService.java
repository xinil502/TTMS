package cn.xinill.ttms.service;

import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOOrder;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/7 23:07
 */
public interface IOrderService {
    /**
     * 添加订单，返回订单id。如果添加失败，则返回 id为 -1.
     * @param order
     * @return
     */
    long addOrder(Order order);

    /**
     * 通过用户id，查询订单信息
     * @param userId
     * @return
     */
    List<VOOrder> getOrderList(int userId);

    /**
     * 通过用户 phone，查询订单信息
     * @param phone
     * @return
     */
    List<VOOrder> getOrderList(String phone);

    /**
     * 退票接口
     * @param orderId
     * @return
     */
    Boolean reverseOrder(Long orderId) throws MyException;
}
