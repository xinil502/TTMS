package cn.xinill.ttms.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 18:01
 */
public class VOTicketList {
    private Integer rowCount;
    private Integer colCount;
    private List<VOTicket> tickets;

    public VOTicketList() {
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getColCount() {
        return colCount;
    }

    public void setColCount(Integer colCount) {
        this.colCount = colCount;
    }

    public List<VOTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<VOTicket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "VOTicketList{" +
                "rowCount=" + rowCount +
                ", colCount=" + colCount +
                ", tickets=" + tickets +
                '}';
    }
}
