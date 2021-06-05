package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.IScheduleMapper;
import cn.xinill.ttms.mapper.ISeatMapper;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.po.Seat;
import cn.xinill.ttms.vo.VOScheduleList;
import cn.xinill.ttms.service.IScheduleService;
import cn.xinill.ttms.service.ITicketService;
import cn.xinill.ttms.utils.MyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 9:05
 */
@Service
@Transactional
public class ScheduleServiceImpl implements IScheduleService {


    private IScheduleMapper scheduleMapper;
    private ISeatMapper seatMapper;
    private ITicketService ticketService;

    @Autowired(required = false)
    public void setScheduleMapper(IScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    @Autowired(required = false)
    public void setSeatMapper(ISeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    @Autowired(required = false)
    public void setTicketService(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public boolean insertSchedule(Schedule schedule) throws MyException{
        if(0 < scheduleMapper.countStudioUsed(schedule.getStudioId(), schedule.getStartTime(), schedule.getEndTime())){
            throw new MyException("演出计划时间段冲突");
        }
        if(1 == scheduleMapper.insertSchedule(schedule)){
            //查询插入的演出计划ID
            schedule.setScheduleId(scheduleMapper.selectIdBySchedule(schedule));
            //查询演出厅座位
            List<Seat> seatList = seatMapper.findSeatListByStudioId(schedule.getStudioId());
            for(Seat seat: seatList){
                ticketService.createTicket(schedule.getScheduleId(), seat.getSeatId());
            }
            return true;
        }
        return false;
    }

    @Override
    public VOScheduleList selectAllScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit) {

        if(sortRule.equals("down")){
            sortRule = "DESC";
        }else{
            sortRule = "ASC";
        }

        VOScheduleList scheduleList = new VOScheduleList();
        scheduleList.setSum(scheduleMapper.countAllSchedule(movieId));
        scheduleList.setSchedule(scheduleMapper.selectAllScheduleList(movieId, sortName, sortRule, (page-1)*pageLimit, pageLimit));
        return scheduleList;
    }

    @Override
    public VOScheduleList selectUserScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit) {

        if(sortRule.equals("down")){
            sortRule = "DESC";
        }else{
            sortRule = "ASC";
        }

        long time = System.currentTimeMillis();

        VOScheduleList scheduleList = new VOScheduleList();
        scheduleList.setSum(scheduleMapper.countUserSchedule(movieId, time));
        scheduleList.setSchedule(scheduleMapper.selectUserScheduleList(movieId, sortName, sortRule, (page-1)*pageLimit, pageLimit, time));
        return scheduleList;
    }

    @Override
    public Schedule updateStatus(int id) {
        Schedule schedule = scheduleMapper.selectScheduleById(id);
        if(schedule == null){
            return null;
        }
        scheduleMapper.changeStatusById(schedule.getScheduleId(), schedule.getStatus()^1);
        schedule.setStatus(schedule.getStatus()^1);
        return schedule;
    }

    @Override
    public Schedule findScheduleById(int scheduleId) {
        return scheduleMapper.selectScheduleById(scheduleId);
    }
}