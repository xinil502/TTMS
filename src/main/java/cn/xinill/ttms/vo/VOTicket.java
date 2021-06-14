package cn.xinill.ttms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:10
 */
public class VOTicket implements Serializable {
    private Integer ticketId;
    private Integer row;
    private Integer col;
    private Integer ticketStatus;
    @JsonIgnore
    private Integer seatStatus;

    public VOTicket() {
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(Integer seatStatus) {
        this.seatStatus = seatStatus;
    }

    @Override
    public String toString() {
        return "VOTicket{" +
                "ticketId=" + ticketId +
                ", row=" + row +
                ", col=" + col +
                ", ticketStatus=" + ticketStatus +
                ", seatStatus=" + seatStatus +
                '}';
    }
}
