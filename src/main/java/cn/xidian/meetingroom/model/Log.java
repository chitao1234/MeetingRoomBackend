package cn.xidian.meetingroom.model;

import lombok.Data;
import java.util.Date;

@Data
public class Log {
    private Long logId;
    private Long userId;
    private String operationType;
    private String operationDetails;
    private Date createdTime;
    private byte[] ipAddress;
}
