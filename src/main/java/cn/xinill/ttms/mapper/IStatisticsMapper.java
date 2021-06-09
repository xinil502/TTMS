package cn.xinill.ttms.mapper;

import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.vo.VOBoxOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 22:54
 */
@Mapper
public interface IStatisticsMapper {

    /**
     * 查询所有电影的票房统计排名
     */
    List<VOBoxOffice> selectAllBoxOffice(int start, int len, String sortRule);

    /**
     * 查询固定时间段内，所有电影的票房统计排名
     */
    List<VOBoxOffice> selectBoxOfficeByTime(int start, int len, String sortRule, long startTime, long endTime);


    /**
     * 查询所有销售订单
     */
    List<Order> getSales();
}
