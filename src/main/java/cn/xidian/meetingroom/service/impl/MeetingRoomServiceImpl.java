package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.MeetingRoomMapper;
import cn.xidian.meetingroom.mapper.ReservationMapper;
import cn.xidian.meetingroom.model.MeetingRoomExample;
import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;
import cn.xidian.meetingroom.model.Reservation;
import cn.xidian.meetingroom.model.ReservationExample;
import cn.xidian.meetingroom.service.MeetingRoomService;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    
    private final MeetingRoomMapper meetingRoomMapper;
    private final ReservationMapper reservationMapper;

    public MeetingRoomServiceImpl(MeetingRoomMapper meetingRoomMapper, ReservationMapper reservationMapper) {
        this.meetingRoomMapper = meetingRoomMapper;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public MeetingRoomWithBLOBs getMeetingRoomById(Integer meetingRoomId) {
        return meetingRoomMapper.selectByPrimaryKey(meetingRoomId);
    }

    @Override
    public MeetingRoomWithBLOBs createMeetingRoom(MeetingRoomWithBLOBs meetingRoom) {
        meetingRoom.setCreatedTime(LocalDateTime.now());
        meetingRoom.setUpdatedTime(LocalDateTime.now());
        meetingRoomMapper.insertSelective(meetingRoom);
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

    @Override
    public List<MeetingRoomWithBLOBs> searchAvailableMeetingRooms(Integer minCapacity, LocalDateTime startTime, LocalDateTime endTime) {
        // Get all meeting rooms that meet the capacity requirement
        List<MeetingRoomWithBLOBs> allRooms = null;
        if (minCapacity != null) {
            System.out.println("minCapacity: " + minCapacity);
            MeetingRoomExample example = new MeetingRoomExample();
            example.createCriteria().andCapacityGreaterThanOrEqualTo(minCapacity);
            allRooms = meetingRoomMapper.selectByExampleWithBLOBs(example);
        } else {
            allRooms = meetingRoomMapper.selectByExampleWithBLOBs(new MeetingRoomExample());
        }

        // log the allRooms
        System.out.println("allRooms: " + allRooms);

        return allRooms.stream()
                .filter(room -> {
                    if (startTime == null && endTime == null) {
                        return true;
                    }

                    ReservationExample example = new ReservationExample();
                    if (endTime != null) {
                        ReservationExample.Criteria criteria1 = example.or();
                        criteria1.andMeetingRoomIdEqualTo(room.getMeetingRoomId())
                                .andStartTimeLessThan(endTime)
                                .andEndTimeGreaterThan(startTime);
                    }
                    if (startTime != null) {
                        ReservationExample.Criteria criteria2 = example.or();
                        criteria2.andMeetingRoomIdEqualTo(room.getMeetingRoomId())
                                .andEndTimeGreaterThan(startTime)
                                .andStartTimeLessThan(endTime);
                    }

                    List<Reservation> conflictingReservations = reservationMapper.selectByExample(example);
                    System.out.println("conflictingReservations: " + conflictingReservations);
                    return conflictingReservations.isEmpty();
                })
                .collect(Collectors.toList());
    }
} 