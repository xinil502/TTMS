package cn.xinill.ttms.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/2 17:44
 */
@Data
public class VOScheduleList {
    private Integer sum;
    private List<VOSchedule> schedule;
}
