package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.ITicketMapper;
import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.po.Studio;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.service.*;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOTicketOrder;
import cn.xinill.ttms.vo.VOTicket;
import cn.xinill.ttms.vo.VOTicketList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 16:59
 */
@Service
@Transactional
public class TicketServiceImpl implements ITicketService {
    private static final Object LOCK = new Object();
    private ITicketMapper ticketMapper;
    private IScheduleService scheduleService;
    private IStudioService studioService;
    private IUserService userService;
    private IOrderService orderService;

    @Autowired(required = false)
    public void setTicketMapper(ITicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Autowired
    public void setStudioService(IStudioService studioService) {
        this.studioService = studioService;
    }

    @Autowired
    public void setScheduleService(IScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public boolean createTicket(int scheduleId, int seatId) {
        return 1 == ticketMapper.insertTicket(scheduleId, seatId);
    }

    @Override
    public VOTicketList findTicketListByScheduleId(int scheduleId){
        VOTicketList voTicketList = new VOTicketList();

        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        Studio studio = studioService.findStudioById(schedule.getStudioId());
        voTicketList.setRowCount(studio.getRow());
        voTicketList.setColCount(studio.getCol());

        List<VOTicket> list = ticketMapper.findTicketByScheduleId(scheduleId);
        for(VOTicket ticket: list){
            if(ticket.getSeatStatus() == 0){ //???????????????????????????????????????????????????????????????
                ticket.setTicketStatus(0);
            }
        }
        voTicketList.setTickets(list);
        return voTicketList;
    }

    @Override
    public Boolean saleTickets(VOTicketOrder saleTicket) throws MyException{
        //??????????????????????????? id
        int userId = userService.logIn(saleTicket.getPhone());
        //??????????????????
        Integer[] tickets = saleTicket.getTickets();
        String ticket = ticketsToString(tickets);
        double change = tickets.length * ticketMapper.getPrice(tickets[0]);
        //????????????????????????
        long scheduleStartTime = scheduleService.findScheduleById(saleTicket.getScheduleId()).getStartTime();

        //??????
        synchronized (TicketServiceImpl.LOCK){
            if(saleTicket.getTickets().length == ticketMapper.countTicketCanBuy(ticket)){
                //????????????
                Order order = new Order();
                order.setUserId(userId);
                order.setScheduleId(saleTicket.getScheduleId());
                order.setPayTime(System.currentTimeMillis());
                order.setPrice(change);
                order.setOrderExistTime(scheduleStartTime-15*60*1000);
                long orderId = orderService.addOrder(order);
                //??????
                ticketMapper.buyTicket(2, ticket);
                //??????order??????ticket
                for(Integer t: tickets){
                    addOrderTicket(orderId, t);
                }
                return true;
            }else{
                throw new MyException("??????????????????");
            }
        }
    }



    @Override
    public Boolean saleTickets(VOTicketOrder saleTicket, int userId) throws MyException{
        //??????????????????
        Integer[] tickets = saleTicket.getTickets();
        String ticket = ticketsToString(tickets);
        double change = tickets.length * ticketMapper.getPrice(tickets[0]);
        //????????????????????????
        long scheduleStartTime = scheduleService.findScheduleById(saleTicket.getScheduleId()).getStartTime();

        //?????????
        synchronized (TicketServiceImpl.LOCK){
            //???????????????????????????
            if(tickets.length == ticketMapper.countTicketCanBuy(ticket)){
                //??????
                userService.updateUserMoney(userId, -change);
                //??????????????????
                Order order = new Order();
                order.setUserId(userId);
                order.setScheduleId(saleTicket.getScheduleId());
                order.setPayTime(System.currentTimeMillis());
                order.setPrice(change);
                order.setOrderExistTime(scheduleStartTime-15*60*1000);
                long orderId = orderService.addOrder(order);
                //??????
                ticketMapper.buyTicket(3, ticket);
                //??????order??????ticket
                for(Integer t: tickets){
                    addOrderTicket(orderId, t);
                }
                return true;
            }else{
                throw new MyException("??????????????????");
            }
        }
    }

    public Boolean addOrderTicket(long orderId, int ticketId){
        return 1 == ticketMapper.addOrderTicket(orderId, ticketId);
    }

    @Override
    public List<VOTicket> findTicketListByOrderId(long orderId){
        return ticketMapper.findTicketByOrderId(orderId);
    }

    @Override
    public Boolean reverseTicket(long orderId){
    	ticketMapper.reverseTicket(orderId);
    	return true;
    }

    String ticketsToString(Integer[] tickets){
        //??????????????????
        StringBuilder sb = new StringBuilder("(");
        for(Integer i: tickets){
            sb.append(i).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(')');
        return sb.toString();
    }

}
