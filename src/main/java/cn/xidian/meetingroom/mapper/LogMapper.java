package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Log;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Date;

@Mapper
public interface LogMapper {
    Log selectLogById(Long logId);
    List<Log> selectLogsByUserId(Long userId);
    List<Log> selectLogsByDateRange(Date startDate, Date endDate);
    List<Log> selectLogsByOperationType(String operationType);
    void insertLog(Log log);
    void deleteLogsBefore(Date date);
}
