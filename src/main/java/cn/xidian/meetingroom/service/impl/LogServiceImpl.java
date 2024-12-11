package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.LogMapper;
import cn.xidian.meetingroom.model.LogExample;
import cn.xidian.meetingroom.model.LogWithBLOBs;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {
    
    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public LogWithBLOBs getLogById(Integer logId) {
        return logMapper.selectByPrimaryKey(logId);
    }

    @Override
    public List<LogWithBLOBs> getLogsByUserId(Integer userId) {
        LogExample example = new LogExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return logMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<LogWithBLOBs> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        LogExample example = new LogExample();
        example.createCriteria().andCreatedTimeBetween(startDate, endDate);
        return logMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<LogWithBLOBs> getLogsByOperationType(String operationType) {
        LogExample example = new LogExample();
        example.createCriteria().andOperationTypeEqualTo(operationType);
        return logMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public LogWithBLOBs createLog(Integer userId, String operationType, String operationDetails, byte[] ipAddress) {
        LogWithBLOBs log = new LogWithBLOBs();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperationDetails(operationDetails);
        log.setIpAddress(ipAddress);
        log.setCreatedTime(LocalDateTime.now());
        
        logMapper.insertSelective(log);
        return log;
    }

    @Override
    public void deleteOldLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        
        LogExample example = new LogExample();
        example.createCriteria().andCreatedTimeLessThan(cutoffDate);
        
        logMapper.deleteByExample(example);
    }
} 