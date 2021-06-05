package cn.xinill.ttms.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 18:01
 */
@Data
public class VOTicketList {
    private Integer rowCount;
    private Integer colCount;
    private List<VOTicket> tickets;
}
