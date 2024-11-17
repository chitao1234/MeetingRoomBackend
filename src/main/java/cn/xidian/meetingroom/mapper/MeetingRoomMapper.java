package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.MeetingRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingRoomMapper {
    MeetingRoom selectMeetingRoomById(int meetingRoomId);
    void insertMeetingRoom(MeetingRoom meetingRoom);
    // Additional CRUD methods
}
