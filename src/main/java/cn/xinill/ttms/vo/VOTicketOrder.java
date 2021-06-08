package cn.xinill.ttms.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:38
 */
@Data
public class VOTicketOrder implements Serializable {
    private Integer[] tickets;
    private Integer scheduleId;
    private String phone;
}
