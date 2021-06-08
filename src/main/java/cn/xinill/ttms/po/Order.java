package cn.xinill.ttms.po;

import lombok.Data;

/**
 * @Author: Xinil
 * @Date: 2021/6/7 23:08
 */
@Data
public class Order {
    private Long orderId;
    private Integer userId;
    private Integer scheduleId;
    private Long payTime;
    private Double price;
    private Integer status;
    private Long OrderExistTime;
}
