package cn.xinill.ttms.controller;

import cn.xinill.ttms.common.ServerResponse;
import cn.xinill.ttms.service.IStatisticsService;
import cn.xinill.ttms.vo.VOBoxOffice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 22:53
 */
@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private IStatisticsService statisticsService;

    @Autowired
    public void setStatisticsService(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getAllBoxOffice")
    public ServerResponse<List<VOBoxOffice>> getAllBoxOffice(@RequestParam Integer page,
                                                             @RequestParam Integer pageLimit,
                                                             @RequestParam String sortType){
        try{
            logger.info("[查询总票房]：page="+page+"  pageLimit="+pageLimit+"  sortType="+sortType);
            List<VOBoxOffice> voBoxOffices = statisticsService.selectAllBoxOffice(page, pageLimit, sortType);
            logger.info("[查询总票房]：查询总票房成功 + " +voBoxOffices);
            return ServerResponse.createBySuccessMsgData("查询总票房成功", voBoxOffices);
        }catch(Exception e){
            logger.error("[查询总票房]：/statistics/getAllBoxOffice  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getTodayBoxOffice")
    public ServerResponse<List<VOBoxOffice>> selectBoxOfficeByTime(@RequestParam Integer page,
                                                                   @RequestParam Integer pageLimit,
                                                                   @RequestParam String sortType,
                                                                   @RequestParam Long time){
        try{
            logger.info("[查询当日票房]：page="+page+"  pageLimit="+pageLimit+"  sortType="+sortType + "   time="+time);
            List<VOBoxOffice> voBoxOffices = statisticsService.selectBoxOfficeByTime(page, pageLimit, sortType, time);
            logger.info("[查询当日票房]：查询当日票房成功 + " +voBoxOffices);
            return ServerResponse.createBySuccessMsgData("查询当日票房成功", voBoxOffices);
        }catch(Exception e){
            logger.error("[查询当日票房]：/statistics/getAllBoxOffice  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/getSalesVolume")
    public ServerResponse<double[]> getSalesVolume(){
        try{
            logger.info("[查询近30天销售额]");
            double[] list = statisticsService.getSalesVolume();
            logger.info("[查询近30天销售额]：查询当日票房成功 + " + Arrays.toString(list));
            return ServerResponse.createBySuccessMsgData("查询销售额成功", list);
        }catch(Exception e){
            logger.error("[查询近30天销售额]：/statistics/getSalesVolume  接口异常");
            e.printStackTrace();
            return ServerResponse.createByErrorMsgData("服务器异常", null);
        }
    }


}
