package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.MeetingRoom;
import java.util.List;

public interface MeetingRoomService {
    MeetingRoom getMeetingRoomById(Long meetingRoomId);
    MeetingRoom createMeetingRoom(MeetingRoom meetingRoom);
    List<MeetingRoom> getAllMeetingRooms();
    MeetingRoom updateMeetingRoom(Long meetingRoomId, MeetingRoom meetingRoom);
    void deleteMeetingRoom(Long meetingRoomId);
} 