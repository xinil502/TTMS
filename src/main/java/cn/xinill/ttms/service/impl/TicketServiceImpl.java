package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.ITicketMapper;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.po.Studio;
import cn.xinill.ttms.service.IScheduleService;
import cn.xinill.ttms.service.IStudioService;
import cn.xinill.ttms.service.ITicketService;
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

    @Override
    public boolean createTicket(int scheduleId, int seatId) {
        return 1 == ticketMapper.insertTicket(scheduleId, seatId);
    }

    @Override
    public VOTicketList findTicketList(int scheduleId){
        VOTicketList voTicketList = new VOTicketList();

        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        System.out.println(schedule);
        System.out.println(schedule.getStudioId());
        Studio studio = studioService.findStudioById(schedule.getStudioId());
        voTicketList.setRowCount(studio.getRow());
        voTicketList.setColCount(studio.getCol());

        List<VOTicket> list = ticketMapper.findTicketByScheduleId(scheduleId);
        for(VOTicket ticket: list){
            if(ticket.getSeatStatus() == 0){ //如果座位的状态为禁用，则设置票的状态为禁用
                ticket.setTicketStatus(0);
            }
            ticket.setSeatStatus(null); //隐藏座位状态属性
        }
        voTicketList.setTickets(list);
        return voTicketList;
    }

    @Override
    public Boolean saleTickets(VOSaleTicket saleTicket) throws MyException{
        Integer[] tickets = saleTicket.getTickets();
        StringBuilder sb = new StringBuilder("(");
        for(Integer i: tickets){
            sb.append(i).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(')');
        //是否已经售票
        synchronized (object){
            if(tickets.length == ticketMapper.countTicketCanBuy(sb.toString())){
                ticketMapper.updateTicket(sb.toString(), saleTicket.getTime(), 3, null);
                return true;
            }else{
                throw new MyException("该票已被出售");
            }
        }
    }
}
