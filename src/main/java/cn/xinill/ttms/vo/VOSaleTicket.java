package cn.xinill.ttms.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:38
 */
@Data
public class VOSaleTicket implements Serializable {
    private Integer[] tickets;
    private Long time;
    private String phone;
}
