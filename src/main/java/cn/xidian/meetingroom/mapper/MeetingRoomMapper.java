package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.model.MeetingRoomExample;
import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MeetingRoomMapper {
    long countByExample(MeetingRoomExample example);

    int deleteByExample(MeetingRoomExample example);

    int deleteByPrimaryKey(Integer meetingRoomId);

    int insert(MeetingRoomWithBLOBs row);

    int insertSelective(MeetingRoomWithBLOBs row);

    List<MeetingRoomWithBLOBs> selectByExampleWithBLOBs(MeetingRoomExample example);

    List<MeetingRoom> selectByExample(MeetingRoomExample example);

    MeetingRoomWithBLOBs selectByPrimaryKey(Integer meetingRoomId);

    int updateByExampleSelective(@Param("row") MeetingRoomWithBLOBs row, @Param("example") MeetingRoomExample example);

    int updateByExampleWithBLOBs(@Param("row") MeetingRoomWithBLOBs row, @Param("example") MeetingRoomExample example);

    int updateByExample(@Param("row") MeetingRoom row, @Param("example") MeetingRoomExample example);

    int updateByPrimaryKeySelective(MeetingRoomWithBLOBs row);

    int updateByPrimaryKeyWithBLOBs(MeetingRoomWithBLOBs row);

    int updateByPrimaryKey(MeetingRoom row);
}