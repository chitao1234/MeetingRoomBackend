package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;

import java.util.List;
import java.time.LocalDate;

public interface ReservationService {
    ReservationWithBLOBs getReservationById(Integer reservationId);
    List<ReservationWithBLOBs> getReservationsByUserId(Integer userId);
    List<ReservationWithBLOBs> getReservationsByMeetingRoomId(Integer meetingRoomId);
    List<ReservationWithBLOBs> getReservationsByDateRange(Integer meetingRoomId, LocalDate startDate, LocalDate endDate);
    ReservationWithBLOBs createReservation(ReservationWithBLOBs reservation);
    ReservationWithBLOBs updateReservation(Integer reservationId, ReservationWithBLOBs reservation);
    void deleteReservation(Integer reservationId);
    ReservationWithBLOBs approveReservation(Integer reservationId);
    ReservationWithBLOBs rejectReservation(Integer reservationId, String rejectionReason);
} 