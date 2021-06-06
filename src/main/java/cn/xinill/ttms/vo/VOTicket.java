package cn.xinill.ttms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:10
 */
@Data
public class VOTicket implements Serializable {
    private Integer ticketId;
    private Integer row;
    private Integer col;
    private Integer ticketStatus;
    @JsonIgnore
    private Integer seatStatus;
}
