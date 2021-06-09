package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.IStatisticsMapper;
import cn.xinill.ttms.po.Order;
import cn.xinill.ttms.service.IStatisticsService;
import cn.xinill.ttms.vo.VOBoxOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 23:20
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {


    IStatisticsMapper statisticsMapper;

    @Autowired(required = false)
    public void setStatisticsMapper(IStatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }


    @Override
    public List<VOBoxOffice> selectAllBoxOffice(int page, int pageLimit, String sortRule) {
        return statisticsMapper.selectAllBoxOffice((page-1)*pageLimit, pageLimit, sortRule);
    }

    @Override
    public List<VOBoxOffice> selectBoxOfficeByTime(int page, int pageLimit, String sortRule, long startTime) {
        return statisticsMapper.selectBoxOfficeByTime((page-1)*pageLimit, pageLimit, sortRule, startTime, startTime+24*60*60*1000);
    }

    @Override
    public double[] getSalesVolume() {
        double[] sales = new double[30];
        int day = (int)(System.currentTimeMillis()/1000/60/60/24);
        List<Order> list = statisticsMapper.getSales();
        for(Order o: list){
            int k = day - (int)(o.getPayTime()/1000/60/60/24);
            if(k < 30 && k > -1){
                sales[k] += o.getPrice();
            }
        }
        return sales;
    }
}
