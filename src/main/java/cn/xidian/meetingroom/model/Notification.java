package cn.xidian.meetingroom.model;

import lombok.Data;
import java.util.Date;

@Data
public class Notification {
    private Long notificationId;
    private Long userId;
    private String type;
    private String content;
    private boolean isRead;
    private Date createdTime;
}
