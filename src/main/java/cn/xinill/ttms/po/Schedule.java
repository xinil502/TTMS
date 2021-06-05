package cn.xinill.ttms.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/5/10 18:25
 */
@Data
public class Schedule implements Serializable {
    private Integer scheduleId;
    private Integer studioId;
    private Integer MovieId;
    private Long startTime;
    private Long endTime;
    private Integer ticketPrice;
    private Integer Status;
}
