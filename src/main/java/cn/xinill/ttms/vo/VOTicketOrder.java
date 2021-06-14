package cn.xinill.ttms.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author: Xinil
 * @Date: 2021/6/5 17:38
 */
public class VOTicketOrder implements Serializable {
    private Integer[] tickets;
    private Integer scheduleId;
    private String phone;

    public VOTicketOrder() {
    }

    public Integer[] getTickets() {
        return tickets;
    }

    public void setTickets(Integer[] tickets) {
        this.tickets = tickets;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "VOTicketOrder{" +
                "tickets=" + Arrays.toString(tickets) +
                ", scheduleId=" + scheduleId +
                ", phone='" + phone + '\'' +
                '}';
    }
}
