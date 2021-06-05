package cn.xinill.ttms.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Xinil
 * @Date: 2021/2/28 14:22
 */
@Data
public class VOLogin implements Serializable {
    private String phone;
    private String code;
}
