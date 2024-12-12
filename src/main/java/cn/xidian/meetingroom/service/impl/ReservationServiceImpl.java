package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.ReservationMapper;
import cn.xidian.meetingroom.model.Reservation;
import cn.xidian.meetingroom.model.ReservationExample;
import cn.xidian.meetingroom.model.ReservationWithBLOBs;
import cn.xidian.meetingroom.service.ReservationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationWithBLOBs getReservationById(Long reservationId) {
        return reservationMapper.selectByPrimaryKey(reservationId);
    }

    @Override
    public List<ReservationWithBLOBs> getReservationsByUserId(Integer userId) {
        ReservationExample example = new ReservationExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return reservationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ReservationWithBLOBs> getReservationsByMeetingRoomId(Integer meetingRoomId) {
        ReservationExample example = new ReservationExample();
        example.createCriteria().andMeetingRoomIdEqualTo(meetingRoomId);
        return reservationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ReservationWithBLOBs> getReservationsByTimeRange(Integer meetingRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        ReservationExample example = new ReservationExample();
        example.createCriteria()
            .andMeetingRoomIdEqualTo(meetingRoomId)
            .andStartTimeGreaterThan(startTime)
            .andEndTimeLessThan(endTime);
        return reservationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public ReservationWithBLOBs createReservation(ReservationWithBLOBs reservation) {
        reservation.setStatus("PENDING");
        reservation.setCreatedTime(LocalDateTime.now());
        reservationMapper.insertSelective(reservation);
        return reservation;
    }

    @Override
    public ReservationWithBLOBs updateReservation(Long reservationId, ReservationWithBLOBs reservation) {
        Reservation existingReservation = reservationMapper.selectByPrimaryKey(reservationId);
        if (existingReservation == null) {
            return null;
        }
        
        reservation.setReservationId(reservationId);
        reservation.setCreatedTime(existingReservation.getCreatedTime());
        reservationMapper.updateByPrimaryKeySelective(reservation);
        
        return reservationMapper.selectByPrimaryKey(reservationId);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        reservationMapper.deleteByPrimaryKey(reservationId);
    }

    @Override
    public ReservationWithBLOBs approveReservation(Long reservationId) {
        ReservationWithBLOBs reservation = new ReservationWithBLOBs();
        reservation.setReservationId(reservationId);
        reservation.setStatus("APPROVED");
        reservation.setApprovalTime(LocalDateTime.now());
        reservationMapper.updateByPrimaryKeySelective(reservation);
        return reservationMapper.selectByPrimaryKey(reservationId);
    }

    @Override
    public ReservationWithBLOBs rejectReservation(Long reservationId, String rejectionReason) {
        ReservationWithBLOBs reservation = new ReservationWithBLOBs();
        reservation.setReservationId(reservationId);
        reservation.setStatus("REJECTED");
        reservation.setRejectionReason(rejectionReason);
        reservation.setApprovalTime(LocalDateTime.now());
        reservationMapper.updateByPrimaryKeySelective(reservation);
        return reservationMapper.selectByPrimaryKey(reservationId);
    }

    @Override
    public List<ReservationWithBLOBs> getAllReservations() {
        ReservationExample example = new ReservationExample();
        return reservationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ReservationWithBLOBs> getAllReservationsByTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ReservationExample example = new ReservationExample();
        example.createCriteria()
                .andStartTimeBetween(startDateTime, endDateTime)
                .andEndTimeBetween(startDateTime, endDateTime);
        return reservationMapper.selectByExampleWithBLOBs(example);
    }
} 