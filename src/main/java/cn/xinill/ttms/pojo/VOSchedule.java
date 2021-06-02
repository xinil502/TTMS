package cn.xinill.ttms.pojo;

import lombok.Data;

/**
 * @Author: Xinil
 * @Date: 2021/5/31 10:14
 */
@Data
public class VOSchedule{
    private Integer scheduleId;
    private Integer movieId;
    private Integer studioId;
    private String movieName;
    private String studioName;
    private Integer status;
    private Long startTime;
    private Long endTime;
}
