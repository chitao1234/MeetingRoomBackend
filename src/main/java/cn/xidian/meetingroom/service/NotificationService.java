package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification getNotificationById(Integer notificationId);
    List<Notification> getNotificationsByUserId(Integer userId);
    List<Notification> getUnreadNotifications(Integer userId);
    Notification createNotification(Notification notification);
    void markAsRead(Integer notificationId);
    void markAllAsRead(Integer userId);
    void deleteNotification(Integer notificationId);
} 