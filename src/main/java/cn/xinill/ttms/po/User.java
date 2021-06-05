package cn.xinill.ttms.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Integer userId;
    private String username;
    private String phone;
    private String password;
    private String portrait;
    private Integer age;
    private String gender;
    private String email;
    private String introduce;
}
