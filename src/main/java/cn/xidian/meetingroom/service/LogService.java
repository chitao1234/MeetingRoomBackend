package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.LogWithBLOBs;

import java.util.List;
import java.time.LocalDateTime;

public interface LogService {
    LogWithBLOBs getLogById(Integer logId);
    List<LogWithBLOBs> getLogsByUserId(Integer userId);
    List<LogWithBLOBs> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<LogWithBLOBs> getLogsByOperationType(String operationType);
    LogWithBLOBs createLog(Integer userId, String operationType, String operationDetails, byte[] ipAddress);
    void deleteOldLogs(int daysToKeep);
} 