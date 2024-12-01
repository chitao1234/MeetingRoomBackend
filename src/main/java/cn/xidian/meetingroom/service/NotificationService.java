package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification getNotificationById(Long notificationId);
    List<Notification> getNotificationsByUserId(Long userId);
    List<Notification> getUnreadNotifications(Long userId);
    Notification createNotification(Notification notification);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long notificationId);
} 