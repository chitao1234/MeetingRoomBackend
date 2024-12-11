package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;
import cn.xidian.meetingroom.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationWithBLOBs> getReservation(@PathVariable("id") Integer reservationId) {
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(reservationService.getReservationsByDateRange(roomId, startDate, endDate));
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
            @PathVariable("id") Integer reservationId,
            @RequestBody ReservationWithBLOBs reservation) {
        ReservationWithBLOBs updatedReservation = reservationService.updateReservation(reservationId, reservation);
        if (updatedReservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Integer reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReservationWithBLOBs> approveReservation(@PathVariable("id") Integer reservationId) {
        ReservationWithBLOBs reservation = reservationService.approveReservation(reservationId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ReservationWithBLOBs> rejectReservation(
            @PathVariable("id") Integer reservationId,
            @RequestParam String reason) {
        ReservationWithBLOBs reservation = reservationService.rejectReservation(reservationId, reason);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }
} 