package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.ReservationMapper;
import cn.xidian.meetingroom.model.Reservation;
import cn.xidian.meetingroom.service.ReservationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationMapper.selectReservationById(reservationId);
    }

    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationMapper.selectReservationsByUserId(userId);
    }

    @Override
    public List<Reservation> getReservationsByMeetingRoomId(Long meetingRoomId) {
        return reservationMapper.selectReservationsByMeetingRoomId(meetingRoomId);
    }

    @Override
    public List<Reservation> getReservationsByDateRange(Long meetingRoomId, Date startDate, Date endDate) {
        return reservationMapper.selectReservationsByDateRange(meetingRoomId, startDate, endDate);
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus("PENDING");
        reservation.setCreatedTime(new Date());
        reservationMapper.insertReservation(reservation);
        return reservation;
    }

    @Override
    public Reservation updateReservation(Long reservationId, Reservation reservation) {
        Reservation existingReservation = reservationMapper.selectReservationById(reservationId);
        if (existingReservation == null) {
            return null;
        }
        
        Reservation updateReservation = new Reservation();
        updateReservation.setReservationId(reservationId);
        updateReservation.setCreatedTime(existingReservation.getCreatedTime());
        
        if (reservation.getMeetingDate() != null) {
            updateReservation.setMeetingDate(reservation.getMeetingDate());
        }
        if (reservation.getStartTime() != null) {
            updateReservation.setStartTime(reservation.getStartTime());
        }
        if (reservation.getEndTime() != null) {
            updateReservation.setEndTime(reservation.getEndTime());
        }
        if (reservation.getParticipantCount() != null) {
            updateReservation.setParticipantCount(reservation.getParticipantCount());
        }
        if (reservation.getMeetingSubject() != null) {
            updateReservation.setMeetingSubject(reservation.getMeetingSubject());
        }
        if (reservation.getStatus() != null) {
            updateReservation.setStatus(reservation.getStatus());
        }
        if (reservation.getRejectionReason() != null) {
            updateReservation.setRejectionReason(reservation.getRejectionReason());
        }
        
        reservationMapper.updateReservation(updateReservation);
        return reservationMapper.selectReservationById(reservationId);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        reservationMapper.deleteReservation(reservationId);
    }

    @Override
    public Reservation approveReservation(Long reservationId) {
        Reservation reservation = reservationMapper.selectReservationById(reservationId);
        if (reservation == null) {
            return null;
        }
        
        reservation.setStatus("APPROVED");
        reservation.setApprovalTime(new Date());
        reservationMapper.updateReservation(reservation);
        return reservation;
    }

    @Override
    public Reservation rejectReservation(Long reservationId, String rejectionReason) {
        Reservation reservation = reservationMapper.selectReservationById(reservationId);
        if (reservation == null) {
            return null;
        }
        
        reservation.setStatus("REJECTED");
        reservation.setRejectionReason(rejectionReason);
        reservation.setApprovalTime(new Date());
        reservationMapper.updateReservation(reservation);
        return reservation;
    }
} 