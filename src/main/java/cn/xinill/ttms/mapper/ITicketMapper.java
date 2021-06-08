package cn.xinill.ttms.mapper;

import cn.xinill.ttms.vo.VOTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 16:41
 */
@Mapper
public interface ITicketMapper {
    /**
     * 初始化影票
     * @param scheduleId 演出计划id
     * @param seatId 座位 id
     * @return
     */
    int insertTicket(int scheduleId, int seatId);

    /**
     * 根据演出计划 id 查找所有该演出计划的影票
     * @param scheduleId
     * @return
     */
    List<VOTicket> findTicketByScheduleId(int scheduleId);

    /**
     * 查询这些票中有多少票能买
     * @param ticketsId
     * @return
     */
    int countTicketCanBuy(String ticketsId);

    /**
     * 买影票状态
     * @param ticketStatus 0未出售 1线上支付 2线下支付
     * @param ticketsId
     * @return
     */
    int buyTicket(int ticketStatus, String ticketsId);


    /**
     * 添加 OrderTicket记录
     * @param orderId
     * @param ticketId
     * @return
     */
    int addOrderTicket(long orderId, int ticketId);


    /**
     * 根据票获取票的价格
     * @param ticketId
     * @return
     */
    double getPrice(int ticketId);

    /**
     * 根据演出计划 id 查找所有该演出计划的影票
     * @param orderId
     * @return
     */
    List<VOTicket> findTicketByOrderId(long orderId);

    /**
     * 退电影票
     * @param orderId
     * @return
     */
    int reverseTicket(long orderId);
}
