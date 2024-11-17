package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.MeetingRoom;

public interface MeetingRoomService {
    MeetingRoom getMeetingRoomById(int meetingRoomId);
    MeetingRoom createMeetingRoom(MeetingRoom meetingRoom);
} 