package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.ITicketMapper;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.po.Studio;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.service.IScheduleService;
import cn.xinill.ttms.service.IStudioService;
import cn.xinill.ttms.service.ITicketService;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.vo.VOSaleTicket;
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
    static final Object object = new Object();
    private ITicketMapper ticketMapper;
    private IScheduleService scheduleService;
    private IStudioService studioService;
    private IUserService userService;


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

    @Override
    public boolean createTicket(int scheduleId, int seatId) {
        return 1 == ticketMapper.insertTicket(scheduleId, seatId);
    }

    @Override
    public VOTicketList findTicketList(int scheduleId){
        VOTicketList voTicketList = new VOTicketList();

        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        Studio studio = studioService.findStudioById(schedule.getStudioId());
        voTicketList.setRowCount(studio.getRow());
        voTicketList.setColCount(studio.getCol());

        List<VOTicket> list = ticketMapper.findTicketByScheduleId(scheduleId);
        for(VOTicket ticket: list){
            if(ticket.getSeatStatus() == 0){ //如果座位的状态为禁用，则设置票的状态为禁用
                ticket.setTicketStatus(0);
            }
        }
        voTicketList.setTickets(list);
        return voTicketList;
    }

    @Override
    public Boolean saleTickets(VOSaleTicket saleTicket) throws MyException{
        //根据手机号获取用户 id
        int userId = userService.logIn(saleTicket.getPhone());
        //处理购票列表
        String ticket = ticketsToString(saleTicket.getTickets());

        //购票
        synchronized (object){
            if(saleTicket.getTickets().length == ticketMapper.countTicketCanBuy(ticket)){
                ticketMapper.updateTicket(ticket, saleTicket.getTime(), 3, userId);
                return true;
            }else{
                throw new MyException("该票已被出售");
            }
        }
    }


    @Override
    public Boolean saleTickets(VOSaleTicket saleTicket, int userId) throws MyException{
        //处理购票列表
        Integer[] tickets = saleTicket.getTickets();
        String ticket = ticketsToString(tickets);

        double change = tickets.length * ticketMapper.getPrice(tickets[0]);

        //悲观锁
        synchronized (object){
            //收款
            User user = userService.findUserByUid(userId);
            if(user.getBalance() < change){
                throw new MyException("余额不足，请前往前台充值");
            }else{
                user.setBalance(user.getBalance()-change);
            }
            //购票
            if(tickets.length == ticketMapper.countTicketCanBuy(ticket)){
                ticketMapper.updateTicket(ticket, saleTicket.getTime(), 3, userId);
                userService.updateUserInform(user);
                return true;
            }else{
                throw new MyException("该票已被出售");
            }
        }
    }

    String ticketsToString(Integer[] tickets){
        //处理购票列表
        StringBuilder sb = new StringBuilder("(");
        for(Integer i: tickets){
            sb.append(i).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(')');
        return sb.toString();
    }
}
