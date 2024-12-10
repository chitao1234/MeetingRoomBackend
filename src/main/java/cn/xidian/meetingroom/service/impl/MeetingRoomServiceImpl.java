package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.MeetingRoomMapper;
import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.model.MeetingRoomExample;
import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;
import cn.xidian.meetingroom.service.MeetingRoomService;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    
    private final MeetingRoomMapper meetingRoomMapper;

    public MeetingRoomServiceImpl(MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomMapper = meetingRoomMapper;
    }

    @Override
    public MeetingRoomWithBLOBs getMeetingRoomById(Integer meetingRoomId) {
        return meetingRoomMapper.selectByPrimaryKey(meetingRoomId);
    }

    @Override
    public MeetingRoomWithBLOBs createMeetingRoom(MeetingRoomWithBLOBs meetingRoom) {
        meetingRoom.setCreatedTime(LocalDateTime.now());
        meetingRoom.setUpdatedTime(LocalDateTime.now());
        meetingRoomMapper.insert(meetingRoom);
        return meetingRoom;
    }

    @Override
    public List<MeetingRoomWithBLOBs> getAllMeetingRooms() {
        return meetingRoomMapper.selectByExampleWithBLOBs(new MeetingRoomExample());
    }

    @Override
    public MeetingRoomWithBLOBs updateMeetingRoom(Integer meetingRoomId, MeetingRoomWithBLOBs meetingRoom) {
        MeetingRoomWithBLOBs existingRoom = meetingRoomMapper.selectByPrimaryKey(meetingRoomId);
        if (existingRoom == null) {
            return null;
        }
        
        meetingRoom.setMeetingRoomId(meetingRoomId);
        meetingRoom.setCreatedTime(existingRoom.getCreatedTime());
        meetingRoom.setUpdatedTime(LocalDateTime.now());
        meetingRoomMapper.updateByPrimaryKeySelective(meetingRoom);
        
        return meetingRoomMapper.selectByPrimaryKey(meetingRoomId);
    }

    @Override
    public void deleteMeetingRoom(Integer meetingRoomId) {
        meetingRoomMapper.deleteByPrimaryKey(meetingRoomId);
    }
} 