package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;

import java.util.List;
import java.time.LocalDateTime;

public interface ReservationService {
    ReservationWithBLOBs getReservationById(Long reservationId);
    List<ReservationWithBLOBs> getReservationsByUserId(Integer userId);
    List<ReservationWithBLOBs> getReservationsByMeetingRoomId(Integer meetingRoomId);
    List<ReservationWithBLOBs> getReservationsByTimeRange(Integer meetingRoomId, LocalDateTime startTime, LocalDateTime endTime);
    ReservationWithBLOBs createReservation(ReservationWithBLOBs reservation);
    ReservationWithBLOBs updateReservation(Long reservationId, ReservationWithBLOBs reservation);
    void deleteReservation(Long reservationId);
    ReservationWithBLOBs approveReservation(Long reservationId);
    ReservationWithBLOBs rejectReservation(Long reservationId, String rejectionReason);
    List<ReservationWithBLOBs> getAllReservations();
    List<ReservationWithBLOBs> getAllReservationsByTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime);
} 