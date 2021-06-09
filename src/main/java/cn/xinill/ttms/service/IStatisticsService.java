package cn.xinill.ttms.service;

import cn.xinill.ttms.vo.VOBoxOffice;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 23:20
 */
public interface IStatisticsService {

    /**
     * 查询所有电影的票房统计排名
     */
    List<VOBoxOffice> selectAllBoxOffice(int page, int pageLimit, String sortRule);

    /**
     * 查询一天内，所有电影的票房统计排名
     */
    List<VOBoxOffice> selectBoxOfficeByTime(int page, int pageLimit, String sortRule, long startTime);


    /**
     * 查询销售额
     */
    double[] getSalesVolume();
}
