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
import java.util.Date;

@Service
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationWithBLOBs getReservationById(Integer reservationId) {
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
    public List<ReservationWithBLOBs> getReservationsByDateRange(Integer meetingRoomId, LocalDate startDate, LocalDate endDate) {
        ReservationExample example = new ReservationExample();
        example.createCriteria()
            .andMeetingRoomIdEqualTo(meetingRoomId)
            .andMeetingDateBetween(startDate, endDate);
        return reservationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public ReservationWithBLOBs createReservation(ReservationWithBLOBs reservation) {
        reservation.setStatus("PENDING");
        reservation.setCreatedTime(LocalDateTime.now());
        reservationMapper.insert(reservation);
        return reservation;
    }

    @Override
    public ReservationWithBLOBs updateReservation(Integer reservationId, ReservationWithBLOBs reservation) {
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
    public void deleteReservation(Integer reservationId) {
        reservationMapper.deleteByPrimaryKey(reservationId);
    }

    @Override
    public ReservationWithBLOBs approveReservation(Integer reservationId) {
        ReservationWithBLOBs reservation = new ReservationWithBLOBs();
        reservation.setReservationId(reservationId);
        reservation.setStatus("APPROVED");
        reservation.setApprovalTime(LocalDateTime.now());
        reservationMapper.updateByPrimaryKeySelective(reservation);
        return reservationMapper.selectByPrimaryKey(reservationId);
    }

    @Override
    public ReservationWithBLOBs rejectReservation(Integer reservationId, String rejectionReason) {
        ReservationWithBLOBs reservation = new ReservationWithBLOBs();
        reservation.setReservationId(reservationId);
        reservation.setStatus("REJECTED");
        reservation.setRejectionReason(rejectionReason);
        reservation.setApprovalTime(LocalDateTime.now());
        reservationMapper.updateByPrimaryKeySelective(reservation);
        return reservationMapper.selectByPrimaryKey(reservationId);
    }
} 