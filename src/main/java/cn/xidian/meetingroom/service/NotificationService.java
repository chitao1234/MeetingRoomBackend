package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification getNotificationById(Long notificationId);
    List<Notification> getNotificationsByUserId(Integer userId);
    List<Notification> getUnreadNotifications(Integer userId);
    Notification createNotification(Notification notification);
    void markAsRead(Long notificationId);
    void markAllAsRead(Integer userId);
    void deleteNotification(Long notificationId);
}