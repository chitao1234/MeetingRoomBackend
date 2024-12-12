package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Log;
import cn.xidian.meetingroom.model.LogExample;
import cn.xidian.meetingroom.model.LogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogMapper {
    long countByExample(LogExample example);

    int deleteByExample(LogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(LogWithBLOBs row);

    int insertSelective(LogWithBLOBs row);

    List<LogWithBLOBs> selectByExampleWithBLOBs(LogExample example);

    List<Log> selectByExample(LogExample example);

    LogWithBLOBs selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("row") LogWithBLOBs row, @Param("example") LogExample example);

    int updateByExampleWithBLOBs(@Param("row") LogWithBLOBs row, @Param("example") LogExample example);

    int updateByExample(@Param("row") Log row, @Param("example") LogExample example);

    int updateByPrimaryKeySelective(LogWithBLOBs row);

    int updateByPrimaryKeyWithBLOBs(LogWithBLOBs row);

    int updateByPrimaryKey(Log row);
}