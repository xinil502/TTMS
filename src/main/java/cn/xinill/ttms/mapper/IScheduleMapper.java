package cn.xinill.ttms.mapper;

import cn.xinill.ttms.pojo.Schedule;
import cn.xinill.ttms.pojo.VOSchedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 8:18
 */
@Mapper
public interface IScheduleMapper {

    /**
     * 插入新的演出计划
     */
    int insertSchedule(Schedule schedule);


    /**
     * 查询演出计划列表
     */
    List<VOSchedule> selectAllScheduleList(int movieId, String sortName, String sortRule, int index, int len);

    /**
     * 查询演出计划列表
     */
    List<VOSchedule> selectUserScheduleList(int movieId, String sortName, String sortRule, int index, int len, long time);

    /**
     * 根据 id查询演出计划
     */
    Schedule selectScheduleById(int id);

    /**
     * 根据 id修改演出厅状态
     */
    int changeStatusById(int id, int status);
}