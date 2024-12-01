package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.MeetingRoomMapper;
import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.service.MeetingRoomService;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    
    private final MeetingRoomMapper meetingRoomMapper;

    public MeetingRoomServiceImpl(MeetingRoomMapper meetingRoomMapper) {
        this.meetingRoomMapper = meetingRoomMapper;
    }

    @Override
    public MeetingRoom getMeetingRoomById(Long meetingRoomId) {
        return meetingRoomMapper.selectMeetingRoomById(meetingRoomId);
    }

    @Override
    public MeetingRoom createMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoom.setCreatedTime(new Date());
        meetingRoom.setUpdatedTime(new Date());
        meetingRoomMapper.insertMeetingRoom(meetingRoom);
        return meetingRoom;
    }

    @Override
    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomMapper.selectAllMeetingRooms();
    }

    @Override
    public MeetingRoom updateMeetingRoom(Long meetingRoomId, MeetingRoom meetingRoom) {
        MeetingRoom existingRoom = meetingRoomMapper.selectMeetingRoomById(meetingRoomId);
        if (existingRoom == null) {
            return null;
        }
        
        MeetingRoom updateRoom = new MeetingRoom();
        updateRoom.setMeetingRoomId(meetingRoomId);
        updateRoom.setCreatedTime(existingRoom.getCreatedTime());
        updateRoom.setUpdatedTime(new Date());
        
        if (meetingRoom.getName() != null) {
            updateRoom.setName(meetingRoom.getName());
        }
        if (meetingRoom.getRoomNumber() != null) {
            updateRoom.setRoomNumber(meetingRoom.getRoomNumber());
        }
        if (meetingRoom.getCapacity() != null) {
            updateRoom.setCapacity(meetingRoom.getCapacity());
        }
        if (meetingRoom.getArea() != null) {
            updateRoom.setArea(meetingRoom.getArea());
        }
        if (meetingRoom.getPhotoUrl() != null) {
            updateRoom.setPhotoUrl(meetingRoom.getPhotoUrl());
        }
        if (meetingRoom.getDescription() != null) {
            updateRoom.setDescription(meetingRoom.getDescription());
        }
        
        meetingRoomMapper.updateMeetingRoom(updateRoom);
        return meetingRoomMapper.selectMeetingRoomById(meetingRoomId);
    }

    @Override
    public void deleteMeetingRoom(Long meetingRoomId) {
        meetingRoomMapper.deleteMeetingRoom(meetingRoomId);
    }
} 