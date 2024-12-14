package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.ReservationWithBLOBs;
import cn.xidian.meetingroom.service.ReservationService;
import cn.xidian.meetingroom.service.NotificationService;
import cn.xidian.meetingroom.service.MeetingRoomService;
import cn.xidian.meetingroom.model.Notification;
import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.service.UserService;
import cn.xidian.meetingroom.util.IpUtil;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final LogService logService;
    private final MeetingRoomService meetingRoomService;
    private final HttpServletRequest request;

    public ReservationController(ReservationService reservationService, 
                               NotificationService notificationService,
                               UserService userService,
                               LogService logService,
                               MeetingRoomService meetingRoomService,
                               HttpServletRequest request) {
        this.reservationService = reservationService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.logService = logService;
        this.meetingRoomService = meetingRoomService;
        this.request = request;
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

    @GetMapping
    public ResponseEntity<List<ReservationWithBLOBs>> getAllReservations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        if (startDateTime != null && endDateTime != null) {
            return ResponseEntity.ok(reservationService.getAllReservationsByTimeRange(startDateTime, endDateTime));
        }
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationWithBLOBs> createReservation(@RequestBody ReservationWithBLOBs reservation) {
        Integer currentUserId = getCurrentUserId();
        logger.info("User {} creating reservation for room {}", currentUserId, reservation.getMeetingRoomId());
        
        if (reservation.getStartTime().isAfter(reservation.getEndTime())) {
            return ResponseEntity.badRequest().body(null);
        }

        if (meetingRoomService.getMeetingRoomById(reservation.getMeetingRoomId()).getCapacity() < reservation.getParticipantCount()) {
            return ResponseEntity.badRequest().body(null);
        }

        ReservationWithBLOBs createdReservation = reservationService.createReservation(reservation);
        
        // Create notification for all admins about new reservation
        List<User> admins = userService.getAllAdmins();
        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setUserId(admin.getUserId());
            notification.setTitle("New Reservation Request");
            notification.setContent("New reservation request for room " + createdReservation.getMeetingRoomId() + 
                                  " by user " + createdReservation.getUserId());
            notification.setIsRead(false);
            notificationService.createNotification(notification);
        }

        // Create audit log
        String details = String.format("Created reservation for room %d by user %d", 
            createdReservation.getMeetingRoomId(), getCurrentUserId());
        logService.createLog(getCurrentUserId(), "CREATE_RESERVATION", details, 
            IpUtil.getIpAddressBytes(request));
        
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

        // Create a notification for the user who made the reservation
        Notification notification = new Notification();
        notification.setUserId(updatedReservation.getUserId());
        notification.setTitle("Reservation Updated");
        notification.setContent("Your reservation for room " + updatedReservation.getMeetingRoomId() + 
                              " has been updated");
        notification.setIsRead(false);
        notificationService.createNotification(notification);

        // Create audit log
        String details = String.format("Updated reservation %d for room %d by user %d", 
            reservationId, updatedReservation.getMeetingRoomId(), getCurrentUserId());
        logService.createLog(getCurrentUserId(), "UPDATE_RESERVATION", details, 
            IpUtil.getIpAddressBytes(request));

        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long reservationId) {
        ReservationWithBLOBs reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            // Create audit log before deletion
            String details = String.format("Deleted reservation %d for room %d by user %d", 
                reservationId, reservation.getMeetingRoomId(), getCurrentUserId());
            logService.createLog(getCurrentUserId(), "DELETE_RESERVATION", details, 
                IpUtil.getIpAddressBytes(request));
        }
        
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReservationWithBLOBs> approveReservation(@PathVariable("id") Long reservationId) {
        ReservationWithBLOBs reservation = reservationService.approveReservation(reservationId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }

        // Create notification for user about approved reservation
        Notification notification = new Notification();
        notification.setUserId(reservation.getUserId());
        notification.setTitle("Reservation Approved");
        notification.setContent("Your reservation for room " + reservation.getMeetingRoomId() + 
                              " has been approved");
        notification.setIsRead(false);
        notificationService.createNotification(notification);

        // Create audit log
        String details = String.format("Approved reservation %d for room %d by user %d", 
            reservationId, reservation.getMeetingRoomId(), getCurrentUserId());
        logService.createLog(getCurrentUserId(), "APPROVE_RESERVATION", details, 
        IpUtil.getIpAddressBytes(request));

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

        // Create notification for user about rejected reservation
        Notification notification = new Notification();
        notification.setUserId(reservation.getUserId());
        notification.setTitle("Reservation Rejected");
        notification.setContent("Your reservation for room " + reservation.getMeetingRoomId() + 
                              " has been rejected. Reason: " + reason);
        notification.setIsRead(false);
        notificationService.createNotification(notification);

        // Create audit log
        String details = String.format("Rejected reservation %d for room %d by user %d. Reason: %s", 
            reservationId, reservation.getMeetingRoomId(), getCurrentUserId(), reason);
        logService.createLog(getCurrentUserId(), "REJECT_RESERVATION", details, 
            IpUtil.getIpAddressBytes(request));

        return ResponseEntity.ok(reservation);
    }
} 