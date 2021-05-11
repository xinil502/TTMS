package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.pojo.Studio;
import cn.xinill.ttms.pojo.VOIntegerId;
import cn.xinill.ttms.service.IStudioService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/8 19:59
 */
@Controller
@RequestMapping(value = "/studio")
public class StudioController {

    private IStudioService studioService;
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public void setStudioService(IStudioService studioService) {
        this.studioService = studioService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/insert", produces="application/json;charset=UTF-8")
    public ServerResponse<Boolean> insertStudio(@RequestBody Studio studio){
        try{
            logger.info("[新增演出厅]："+studio);
            if(studioService.insert(studio)){
                logger.info("[新增演出厅]：新增演出厅成功");
                return ServerResponse.createBySuccessMsgData("新增演出厅成功", true);
            }
            logger.warn("[新增演出厅]：新增演出厅失败");
            return ServerResponse.createByErrorMsgData("新增演出厅失败", false);
        }catch(Exception e){
            logger.error("[新增演出厅]：/studio/insert  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public ServerResponse<Boolean> modifyStudio(@RequestBody Studio studio){
        try{
            logger.info("[修改演出厅]："+studio);
            if(studioService.modifyStudio(studio)){
                logger.info("[修改演出厅]：修改成功");
                return ServerResponse.createBySuccessMsgData("修改演出厅成功", true);
            }
            logger.warn("[修改演出厅]：修改失败");
            return ServerResponse.createByErrorMsgData("修改演出厅失败", false);
        }catch(Exception e){
            logger.error("[修改演出厅]：/studio/modify  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", false);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/query-list")
    public ServerResponse<List<Studio>> findStudio(){
        try{
            logger.info("[查询演出厅列表]：正在查找中...");
            List<Studio> list = studioService.findAllStudio();
            logger.info("[查询演出厅列表]：查询演出厅成功");
            return ServerResponse.createBySuccessMsgData("查询演出厅成功", list);
        }catch(Exception e){
            logger.error("[查询演出厅列表]：/studio/query-list  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ServerResponse<Boolean> delete(@RequestBody VOIntegerId studioId){
        try{
            logger.info("[删除演出厅]：演出厅id："+studioId.getId());
            if(studioService.deleteStudio(studioId.getId())){
                logger.info("[删除演出厅]：删除演出厅成功");
                return ServerResponse.createBySuccessMsgData("删除演出厅成功", true);
            }else{
                logger.warn("[删除演出厅]：演出厅不存在");
                return ServerResponse.createBySuccessMsgData("演出厅不存在", true);
            }
        }catch(Exception e){
            logger.error("[删除演出厅]：服务器异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }
}
