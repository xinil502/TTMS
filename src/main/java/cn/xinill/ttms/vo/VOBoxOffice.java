package cn.xinill.ttms.vo;

import lombok.Data;

/**
 * @Author: Xinil
 * @Date: 2021/6/8 22:56
 */
public class VOBoxOffice {
    private String movieName;
    private Integer boxOffice;

    public VOBoxOffice() {
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(Integer boxOffice) {
        this.boxOffice = boxOffice;
    }

    @Override
    public String toString() {
        return "VOBoxOffice{" +
                "movieName='" + movieName + '\'' +
                ", boxOffice=" + boxOffice +
                '}';
    }
}
