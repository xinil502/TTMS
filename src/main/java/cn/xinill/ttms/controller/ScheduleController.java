package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.po.Schedule;
import cn.xinill.ttms.vo.VOIntegerId;
import cn.xinill.ttms.vo.VOSchedule;
import cn.xinill.ttms.vo.VOScheduleList;
import cn.xinill.ttms.service.IScheduleService;
import cn.xinill.ttms.utils.MyException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 9:26
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    private IScheduleService scheduleService;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public void setScheduleService(IScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 修改演出计划状态
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/toggleStatus")
    public ServerResponse<Boolean> changeStatus(@RequestBody VOIntegerId id){
        try {
            logger.info("[更改演出计划状态]： id = "+id.getId());
            Schedule schedule = scheduleService.updateStatus(id.getId());
            if(schedule == null){
                logger.info("[更改演出计划状态]：未查到该演出计划 ：" + schedule);
                return ServerResponse.createBySuccessMsgData("未查到该演出计划",false);
            }
            logger.info("[更改演出计划状态]：成功更改演出计划状态 ：" + schedule);
            return ServerResponse.createBySuccessMsgData("成功更改演出计划状态",true);
        } catch (Exception e) {
            logger.error("[更改演出计划状态]：/schedule/toggleStatus 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", false);
        }
    }

    /**
     * 添加演出计划
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ServerResponse<Boolean> insertSchedule(@RequestBody Schedule schedule) {
        try {
            logger.info("[添加演出计划信息]：" + schedule);
            if (scheduleService.insertSchedule(schedule)) {
                logger.info("[添加演出计划信息]：成功添加演出计划信息");
                return ServerResponse.createBySuccessMsgData("成功添加", true);
            } else {
                logger.warn("[添加演出计划信息]：添加演出计划失败");
                return ServerResponse.createByErrorMsgData("添加失败", false);
            }
        }catch (MyException e){
            logger.warn("[添加演出计划信息]："+e.getMessage());
            return ServerResponse.createByErrorMsgData(e.getMessage(), false);
        }catch (Exception e) {
            logger.error("[添加演出计划信息]：/schedule/add 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", false);
        }
    }

    /**
     * 查询所有演出计划
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/allQuery")
    public ServerResponse<VOScheduleList> getAllScheduleList(@RequestParam int movieId,
                                                           @RequestParam String sortName,
                                                           @RequestParam String sortRule,
                                                           @RequestParam int page,
                                                           @RequestParam int pageLimit){
        try {
            logger.info("[查询所有演出计划信息]：{movieId="+movieId+", sortName="+sortName+", sortRule="+sortRule+", page="+page+", pageLimit="+pageLimit+"}");
            VOScheduleList scheduleList = scheduleService.selectAllScheduleList(movieId, sortName, sortRule, page, pageLimit);
            List<VOSchedule> list = scheduleList.getSchedule();

            StringBuilder sb = new StringBuilder();
            sb.append(scheduleList.getSum()).append(": ");
            for(VOSchedule schedule: list){
                sb.append(schedule.toString())
                        .append(",");
            }
            logger.info("[查询所有演出计划信息]：" + sb.toString());

            return ServerResponse.createBySuccessMsgData("查询成功",scheduleList);
        } catch (Exception e) {
            logger.error("[查询所有演出计划信息]：/schedule/allQuery 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", null);
        }
    }

    /**
     * 查询有效的演出计划
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/userQuery")
    public ServerResponse<VOScheduleList> getUserScheduleList(@RequestParam int movieId,
                                                           @RequestParam String sortName,
                                                           @RequestParam String sortRule,
                                                           @RequestParam int page,
                                                           @RequestParam int pageLimit){
        try {
            logger.info("[用户查询演出计划信息]：{movieId="+movieId+", sortName="+sortName+", sortRule="+sortRule+", page="+page+", pageLimit="+pageLimit+"}");

            VOScheduleList scheduleList = scheduleService.selectUserScheduleList(movieId, sortName, sortRule, page, pageLimit);
            List<VOSchedule> list = scheduleList.getSchedule();

            StringBuilder sb = new StringBuilder();
            sb.append(scheduleList.getSum()).append(": ");
            for(VOSchedule schedule: list){
                sb.append(schedule.toString())
                        .append(",");
            }
            logger.info("[用户查询演出计划信息]：" + sb.toString());

            return ServerResponse.createBySuccessMsgData("查询成功", scheduleList);
        } catch (Exception e) {
            logger.error("[用户查询演出计划信息]：/schedule/userQuery 接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器繁忙", null);
        }
    }


}
