package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.MeetingRoom;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MeetingRoomMapper {
    MeetingRoom selectMeetingRoomById(Long meetingRoomId);
    void insertMeetingRoom(MeetingRoom meetingRoom);
    List<MeetingRoom> selectAllMeetingRooms();
    void updateMeetingRoom(MeetingRoom meetingRoom);
    void deleteMeetingRoom(Long meetingRoomId);
}
