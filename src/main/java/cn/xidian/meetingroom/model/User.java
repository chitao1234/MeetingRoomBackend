package cn.xidian.meetingroom.model;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private Date createdTime;
    private Date lastLoginTime;
}
