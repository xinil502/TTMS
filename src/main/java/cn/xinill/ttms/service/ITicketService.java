package cn.xinill.ttms.service;

import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOTicket;
import cn.xinill.ttms.vo.VOTicketOrder;
import cn.xinill.ttms.vo.VOTicketList;

import java.util.List;


/**
 * @Author: Xinil
 * @Date: 2021/6/5 16:59
 */
public interface ITicketService {
    /**
     * 通过演出计划 id和座位 id，购买影票
     */
    boolean createTicket(int scheduleId, int seatId);

    /**
     * 根据演出计划 id查找对应的演出票
     */
    VOTicketList findTicketListByScheduleId(int scheduleId);

    /**
     * 手机号购票
     */
    Boolean saleTickets(VOTicketOrder saleTicket) throws MyException;

    /**
     * 用户id售票
     */
    Boolean saleTickets(VOTicketOrder saleTicket, int userId) throws MyException;


    Boolean addOrderTicket(long orderId, int ticketId);
    /**
     * 根据订单 id查询电影票
     */
    List<VOTicket> findTicketListByOrderId(long orderId);

    /**
     * 退票
     */
    Boolean reverseTicket(long orderId);
}
