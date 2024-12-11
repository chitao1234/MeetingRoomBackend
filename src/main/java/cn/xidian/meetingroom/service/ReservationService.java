package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;

import java.util.List;
import java.time.LocalDateTime;

public interface ReservationService {
    ReservationWithBLOBs getReservationById(Integer reservationId);
    List<ReservationWithBLOBs> getReservationsByUserId(Integer userId);
    List<ReservationWithBLOBs> getReservationsByMeetingRoomId(Integer meetingRoomId);
    List<ReservationWithBLOBs> getReservationsByTimeRange(Integer meetingRoomId, LocalDateTime startTime, LocalDateTime endTime);
    ReservationWithBLOBs createReservation(ReservationWithBLOBs reservation);
    ReservationWithBLOBs updateReservation(Integer reservationId, ReservationWithBLOBs reservation);
    void deleteReservation(Integer reservationId);
    ReservationWithBLOBs approveReservation(Integer reservationId);
    ReservationWithBLOBs rejectReservation(Integer reservationId, String rejectionReason);
} 