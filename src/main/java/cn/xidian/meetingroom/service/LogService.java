package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.Log;
import java.util.List;
import java.util.Date;

public interface LogService {
    Log getLogById(Long logId);
    List<Log> getLogsByUserId(Long userId);
    List<Log> getLogsByDateRange(Date startDate, Date endDate);
    List<Log> getLogsByOperationType(String operationType);
    Log createLog(Long userId, String operationType, String operationDetails, byte[] ipAddress);
    void deleteOldLogs(int daysToKeep);
} 