package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;
import cn.xidian.meetingroom.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationWithBLOBs> getReservation(@PathVariable("id") Long reservationId) {
        ReservationWithBLOBs reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationWithBLOBs>> getUserReservations(@PathVariable Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ReservationWithBLOBs>> getRoomReservations(
            @PathVariable Integer roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        if (startDateTime != null && endDateTime != null) {
            return ResponseEntity.ok(reservationService.getReservationsByTimeRange(roomId, startDateTime, endDateTime));
        }
        return ResponseEntity.ok(reservationService.getReservationsByMeetingRoomId(roomId));
    }

    @PostMapping
    public ResponseEntity<ReservationWithBLOBs> createReservation(@RequestBody ReservationWithBLOBs reservation) {
        ReservationWithBLOBs createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationWithBLOBs> updateReservation(
            @PathVariable("id") Long reservationId,
            @RequestBody ReservationWithBLOBs reservation) {
        ReservationWithBLOBs updatedReservation = reservationService.updateReservation(reservationId, reservation);
        if (updatedReservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReservationWithBLOBs> approveReservation(@PathVariable("id") Long reservationId) {
        ReservationWithBLOBs reservation = reservationService.approveReservation(reservationId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ReservationWithBLOBs> rejectReservation(
            @PathVariable("id") Long reservationId,
            @RequestParam String reason) {
        ReservationWithBLOBs reservation = reservationService.rejectReservation(reservationId, reason);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }
} 