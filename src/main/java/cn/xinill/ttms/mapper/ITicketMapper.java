package cn.xinill.ttms.mapper;

import cn.xinill.ttms.vo.VOTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 16:41
 */
@Mapper
public interface ITicketMapper {
    /**
     * 初始化影票
     * @param scheduleId 演出计划id
     * @param seatId 座位 id
     * @return
     */
    int insertTicket(int scheduleId, int seatId);

    /**
     * 根据演出计划 id 查找所有该演出计划的影票
     * @param scheduleId
     * @return
     */
    List<VOTicket> findTicketByScheduleId(int scheduleId);

    /**
     * 查询这些票中有多少票能买
     * @param ticketsId
     * @return
     */
    int countTicketCanBuy(String ticketsId);

    /**
     * 修改影票状态
     * @param ticketsId
     * @param time
     * @param ticketStatus 0未出售 1线上支付 2线下支付
     * @param userId 用户购买时传递用户id，其他情况下默认-1
     * @return
     */
    int updateTicket(String ticketsId, long time, int ticketStatus, Integer userId);


    double getPrice(int ticketId);
}
