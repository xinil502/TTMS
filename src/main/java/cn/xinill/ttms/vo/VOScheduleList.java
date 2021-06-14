package cn.xinill.ttms.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Xinil
 * @Date: 2021/6/2 17:44
 */
public class VOScheduleList {
    private Integer sum;
    private List<VOSchedule> schedule;

    public VOScheduleList() {
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public List<VOSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<VOSchedule> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "VOScheduleList{" +
                "sum=" + sum +
                ", schedule=" + schedule +
                '}';
    }
}
