package cn.xinill.ttms.service;

import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.vo.VOScheduleList;
import cn.xinill.ttms.utils.MyException;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 9:04
 */
public interface IScheduleService {

    /**
     * 插入演出计划信息
     * @param schedule
     * @return
     */
    boolean insertSchedule(Schedule schedule) throws MyException;

    /**
     * 查询所有演出计划
     * @param movieId
     * @param sortName
     * @param sortRule
     * @param page
     * @param pageLimit
     * @return
     */
    VOScheduleList selectAllScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit);

    /**
     * 查询用户的演出计划
     * @param movieId
     * @param sortName
     * @param sortRule
     * @param page
     * @param pageLimit
     * @return
     */
    VOScheduleList selectUserScheduleList(int movieId, String sortName, String sortRule, int page, int pageLimit);

    /**
     * 根据 id更换状态
     * @param id
     * @return
     */
    Schedule updateStatus(int id);

    /**
     * 根据演出计划 id查找演出计划
     * @param scheduleId
     * @return
     */
    Schedule findScheduleById(int scheduleId);
}
