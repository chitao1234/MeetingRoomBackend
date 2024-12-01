package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.Reservation;
import java.util.List;
import java.util.Date;

public interface ReservationService {
    Reservation getReservationById(Long reservationId);
    List<Reservation> getReservationsByUserId(Long userId);
    List<Reservation> getReservationsByMeetingRoomId(Long meetingRoomId);
    List<Reservation> getReservationsByDateRange(Long meetingRoomId, Date startDate, Date endDate);
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Long reservationId, Reservation reservation);
    void deleteReservation(Long reservationId);
    Reservation approveReservation(Long reservationId);
    Reservation rejectReservation(Long reservationId, String rejectionReason);
} 
