package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.LogMapper;
import cn.xidian.meetingroom.model.Log;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

@Service
public class LogServiceImpl implements LogService {
    
    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public Log getLogById(Long logId) {
        return logMapper.selectLogById(logId);
    }

    @Override
    public List<Log> getLogsByUserId(Long userId) {
        return logMapper.selectLogsByUserId(userId);
    }

    @Override
    public List<Log> getLogsByDateRange(Date startDate, Date endDate) {
        return logMapper.selectLogsByDateRange(startDate, endDate);
    }

    @Override
    public List<Log> getLogsByOperationType(String operationType) {
        return logMapper.selectLogsByOperationType(operationType);
    }

    @Override
    public Log createLog(Long userId, String operationType, String operationDetails, byte[] ipAddress) {
        Log log = new Log();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperationDetails(operationDetails);
        log.setIpAddress(ipAddress);
        log.setCreatedTime(new Date());
        
        logMapper.insertLog(log);
        return log;
    }

    @Override
    public void deleteOldLogs(int daysToKeep) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysToKeep);
        logMapper.deleteLogsBefore(cal.getTime());
    }
} 