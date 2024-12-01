package cn.xidian.meetingroom.model;

import lombok.Data;
import java.util.Date;

@Data
public class Reservation {
    private Long reservationId;
    private Long userId;
    private Long meetingRoomId;
    private Date meetingDate;
    private Date startTime;
    private Date endTime;
    private Integer participantCount;
    private String meetingSubject;
    private String status;
    private Date createdTime;
    private String rejectionReason;
    private Date approvalTime;
}
