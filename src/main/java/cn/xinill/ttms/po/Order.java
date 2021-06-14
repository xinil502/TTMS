package cn.xinill.ttms.po;

import lombok.Data;

/**
 * @Author: Xinil
 * @Date: 2021/6/7 23:08
 */
public class Order {
    private Long orderId;
    private Integer userId;
    private Integer scheduleId;
    private Long payTime;
    private Double price;
    private Integer status;
    private Long OrderExistTime;

    public Order() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOrderExistTime() {
        return OrderExistTime;
    }

    public void setOrderExistTime(Long orderExistTime) {
        OrderExistTime = orderExistTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", scheduleId=" + scheduleId +
                ", payTime=" + payTime +
                ", price=" + price +
                ", status=" + status +
                ", OrderExistTime=" + OrderExistTime +
                '}';
    }
}
