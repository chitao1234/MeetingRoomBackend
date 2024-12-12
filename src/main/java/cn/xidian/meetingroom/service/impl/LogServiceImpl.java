package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.LogMapper;
import cn.xidian.meetingroom.model.LogExample;
import cn.xidian.meetingroom.model.LogWithBLOBs;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogServiceImpl implements LogService {
    
    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public LogWithBLOBs getLogById(Long logId) {
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
        if (startDate != null) {
            example.createCriteria().andCreatedTimeGreaterThanOrEqualTo(startDate);
        }
        if (endDate != null) {
            example.createCriteria().andCreatedTimeLessThanOrEqualTo(endDate);
        }
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
        if (daysToKeep <= 0) {
            return;
        }

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        logger.info("Cutoff date: {}", cutoffDate);
        LogExample example = new LogExample();
        example.createCriteria().andCreatedTimeLessThan(cutoffDate);
        
        logMapper.deleteByExample(example);
    }

    @Override
    public List<LogWithBLOBs> getAllLogs() {
        return logMapper.selectByExampleWithBLOBs(new LogExample());
    }
} 