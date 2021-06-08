package cn.xinill.ttms.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/5/10 18:44
 */
@Data
public class Ticket implements Serializable {
    private Integer ticketId;
    private Integer user_id;
    private Integer schedule_id;
    private Integer seat_id;
    private Integer ticket_type;
}
