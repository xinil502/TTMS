package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.IScheduleMapper;
import cn.xinill.ttms.pojo.Schedule;
import cn.xinill.ttms.pojo.VOSchedule;
import cn.xinill.ttms.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 9:05
 */
@Service
public class ScheduleServiceImpl implements IScheduleService {


    IScheduleMapper schduleMapper;

    @Autowired(required = false)
    public void setSchduleMapper(IScheduleMapper schduleMapper) {
        this.schduleMapper = schduleMapper;
    }

    @Override
    public boolean insertSchedule(Schedule schedule) {
        return 1 == schduleMapper.insertSchedule(schedule);
    }

    @Override
    public List<VOSchedule> selectAllScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit) {

        if(sortRule.equals("down")){
            sortRule = "DESC";
        }else{
            sortRule = "ASC";
        }
        return schduleMapper.selectAllScheduleList(movieId, sortName, sortRule, (page-1)*pageLimit, pageLimit);
    }

    @Override
    public List<VOSchedule> selectUserScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit) {

        if(sortRule.equals("down")){
            sortRule = "DESC";
        }else{
            sortRule = "ASC";
        }

        long time = System.currentTimeMillis();
        return schduleMapper.selectUserScheduleList(movieId, sortName, sortRule, (page-1)*pageLimit, pageLimit, time);
    }

    @Override
    public Schedule updateStatus(int id) {
        Schedule schedule = schduleMapper.selectScheduleById(id);
        if(schedule == null){
            return null;
        }
        schduleMapper.changeStatusById(schedule.getId(), schedule.getStatus()^1);
        schedule.setStatus(schedule.getStatus()^1);
        return schedule;
    }
}
