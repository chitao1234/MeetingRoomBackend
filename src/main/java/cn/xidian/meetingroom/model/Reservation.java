package cn.xidian.meetingroom.model;

import lombok.Data;
import java.util.Date;

@Data
public class Reservation {
    private int reservationId;
    private int userId;
    private int meetingRoomId;
    private Date meetingDate;
    private Date startTime;
    private Date endTime;
    private int participantCount;
    private String meetingSubject;
    private String status;
    private Date createdTime;
    private String rejectionReason;
    private Date approvalTime;
}
