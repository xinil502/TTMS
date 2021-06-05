package cn.xinill.ttms.service;

import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOSaleTicket;
import cn.xinill.ttms.vo.VOTicketList;


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
    VOTicketList findTicketList(int scheduleId);

    /**
     * 根据演出计划 id查找对应的演出票
     */
    Boolean saleTickets(VOSaleTicket saleTicket) throws MyException;
}
