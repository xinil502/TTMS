package cn.xinill.ttms.po;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author: Xinil
 * @Date: 2021/5/8 19:49
 */
public class Studio implements Serializable {
    private Integer id;
    private String name;
    private Integer row;
    private Integer col;
    private Integer status;

    public Studio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Studio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", row=" + row +
                ", col=" + col +
                ", status=" + status +
                '}';
    }
}
