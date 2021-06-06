package cn.xinill.ttms.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Integer userId;
    private String username;
    private String phone;
    @JsonIgnore
    private String password;
    private String portrait;
    private Integer age;
    private String gender;
    private String email;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    private Date Birthday;
    private String introduce;
    private double balance;
}
