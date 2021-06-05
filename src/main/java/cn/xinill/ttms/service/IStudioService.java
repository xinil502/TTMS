package cn.xinill.ttms.service;

import cn.xinill.ttms.po.Seat;
import cn.xinill.ttms.po.Studio;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/5/8 20:00
 */
public interface IStudioService {

    /**
     * 新增演出厅
     * @param studio
     * @return
     */
    boolean insert(Studio studio);


    /**
     * 修改演出厅
     * @param studio
     * @return
     */
    boolean modifyStudio(Studio studio);

    /**
     * 删除演出厅信息
     * @param studioId
     * @return
     */
    boolean deleteStudio(int studioId);

    /**
     * 查询所有演出厅信息
     * @return
     */
    List<Studio> findAllStudio();

    /**
     * 查询指定演出厅所有座位信息
     */
    List<Seat> findSeatListByStudioId(int studioId);

    /**
     * 修改演出厅座位状态
     */
    boolean updateSeatStatus(Seat seat);


    /**
     * 根据 id查询演出厅信息
     */
    Studio findStudioById(int studioId);
}
