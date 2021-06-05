package cn.xinill.ttms.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 10:14
 */
@Data
public class VOSchedule implements Serializable {
    private Integer scheduleId;
    private Integer movieId;
    private Integer studioId;
    private String movieName;
    private String studioName;
    private Long startTime;
    private Long endTime;
    private Double ticketPrice;
    private Integer status;
}
