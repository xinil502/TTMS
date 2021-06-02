package cn.xinill.ttms.pojo;

import lombok.Data;

/**
 * @Author: Xinil
 * @Date: 2021/5/10 18:25
 */
public class Schedule {
    private Integer id;
    private Integer studioId;
    private Integer MovieId;
    private Long startTime;
    private Long endTime;
    private Integer ticketPrice;
    private Integer Status;

    public Schedule() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public Integer getMovieId() {
        return MovieId;
    }

    public void setMovieId(Integer movieId) {
        MovieId = movieId;
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

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", studioId=" + studioId +
                ", MovieId=" + MovieId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ticketPrice=" + ticketPrice +
                ", Status=" + Status +
                '}';
    }
}
