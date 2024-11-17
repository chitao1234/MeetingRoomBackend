package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.MeetingRoomMapper;
import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.service.MeetingRoomService;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    
    private final MeetingRoomMapper meetingRoomMapper;

    public MeetingRoomServiceImpl(MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomMapper = meetingRoomMapper;
    }

    @Override
    public MeetingRoom getMeetingRoomById(int meetingRoomId) {
        MeetingRoom meetingRoom = meetingRoomMapper.selectMeetingRoomById(meetingRoomId);
        if (meetingRoom == null) {
            return null;
        }
        
        return meetingRoom;
    }

    @Override
    public MeetingRoom createMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoom.setCreatedTime(new Date());
        meetingRoom.setUpdatedTime(new Date());
        meetingRoomMapper.insertMeetingRoom(meetingRoom);
        return meetingRoom;
    }
} 