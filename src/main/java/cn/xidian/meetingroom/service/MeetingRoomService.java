package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;

import java.util.List;
import java.time.LocalDateTime;

public interface MeetingRoomService {
    MeetingRoomWithBLOBs getMeetingRoomById(Integer meetingRoomId);
    MeetingRoomWithBLOBs createMeetingRoom(MeetingRoomWithBLOBs meetingRoom);
    List<MeetingRoomWithBLOBs> getAllMeetingRooms();
    MeetingRoomWithBLOBs updateMeetingRoom(Integer meetingRoomId, MeetingRoomWithBLOBs meetingRoom);
    void deleteMeetingRoom(Integer meetingRoomId);
    List<MeetingRoomWithBLOBs> searchAvailableMeetingRooms(Integer minCapacity, LocalDateTime startTime, LocalDateTime endTime);
} 