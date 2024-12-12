package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.Notification;
import cn.xidian.meetingroom.service.NotificationService;
import cn.xidian.meetingroom.util.IpUtil;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final LogService logService;
    private final HttpServletRequest request;

    public NotificationController(NotificationService notificationService,
                                LogService logService,
                                HttpServletRequest request) {
        this.notificationService = notificationService;
        this.logService = logService;
        this.request = request;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable("id") Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("id") Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if (notification != null) {
            notificationService.markAsRead(notificationId);
            
            // Create audit log
            String details = String.format("Marked notification %d as read", notificationId);
            logService.createLog(notification.getUserId(), "MARK_NOTIFICATION_READ", details, 
                IpUtil.getIpAddressBytes(request));
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Integer userId) {
        notificationService.markAllAsRead(userId);
        
        // Create audit log
        String details = String.format("Marked all notifications as read for user %d", userId);
        logService.createLog(userId, "MARK_ALL_NOTIFICATIONS_READ", details, 
            IpUtil.getIpAddressBytes(request));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId);
        if (notification != null) {
            // Create audit log before deletion
            String details = String.format("Deleted notification %d", notificationId);
            logService.createLog(notification.getUserId(), "DELETE_NOTIFICATION", details, 
                IpUtil.getIpAddressBytes(request));
        }

        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
} 