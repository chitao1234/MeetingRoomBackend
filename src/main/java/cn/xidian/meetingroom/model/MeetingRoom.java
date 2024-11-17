package cn.xidian.meetingroom.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MeetingRoom {
    private int meetingRoomId;
    private String name;
    private String roomNumber;
    private int capacity;
    private BigDecimal area;
    private String photoUrl;
    private String description;
    private Date createdTime;
    private Date updatedTime;
}
