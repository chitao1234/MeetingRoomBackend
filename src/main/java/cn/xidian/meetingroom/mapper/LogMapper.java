package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    Log selectLogById(int logId);
    // Additional CRUD methods
}
