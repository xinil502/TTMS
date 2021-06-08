package cn.xinill.ttms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 0:03
 */
@Data
public class VOOrder {
    private Long orderId;
    private Long payTime;
    private double price;
    private String movieName;
    private String studioName;
    private Long startTime;
    private Long endTime;
    private Integer orderStatus;
    @JsonIgnore
    private Long OrderExistTime;
    private List<VOTicket> tickets;
}
