package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.NotificationMapper;
import cn.xidian.meetingroom.model.Notification;
import cn.xidian.meetingroom.service.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationMapper.selectNotificationById(notificationId);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationMapper.selectNotificationsByUserId(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationMapper.selectUnreadNotificationsByUserId(userId);
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreatedTime(new Date());
        notification.setRead(false);
        notificationMapper.insertNotification(notification);
        return notification;
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationMapper.markAsRead(notificationId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationMapper.deleteNotification(notificationId);
    }

    // Utility method to create different types of notifications
    public void createSystemNotification(Long userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("SYSTEM");
        notification.setContent(content);
        createNotification(notification);
    }

    public void createReservationNotification(Long userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("RESERVATION");
        notification.setContent(content);
        createNotification(notification);
    }

    public void createMeetingRoomNotification(Long userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("MEETING_ROOM");
        notification.setContent(content);
        createNotification(notification);
    }
} 