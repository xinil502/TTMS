package cn.xinill.ttms.mapper;

import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.vo.VOOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/7 22:52
 */
@Mapper
public interface IOrderMapper {
    /**
     * 增加订单
     * @param order
     * @return
     */
    int addOrder(Order order);

    /**
     * 通过用户id获取订单列表
     * @param userId
     * @return
     */
    List<VOOrder> selectOrderByUserId(int userId);

    /**
     * 通过订单 id 获取订单信息
     * @param orderId
     * @return
     */
    Order selectUserIdByOrderId(long orderId);

    /**
     * 修改订单状态
     * @param order
     * @return
     */
    int updateStatus(Order order);
}
