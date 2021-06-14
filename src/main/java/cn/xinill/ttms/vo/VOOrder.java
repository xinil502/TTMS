package cn.xinill.ttms.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 0:03
 */
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

    public VOOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderExistTime() {
        return OrderExistTime;
    }

    public void setOrderExistTime(Long orderExistTime) {
        OrderExistTime = orderExistTime;
    }

    public List<VOTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<VOTicket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "VOOrder{" +
                "orderId=" + orderId +
                ", payTime=" + payTime +
                ", price=" + price +
                ", movieName='" + movieName + '\'' +
                ", studioName='" + studioName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", orderStatus=" + orderStatus +
                ", OrderExistTime=" + OrderExistTime +
                ", tickets=" + tickets +
                '}';
    }
}
