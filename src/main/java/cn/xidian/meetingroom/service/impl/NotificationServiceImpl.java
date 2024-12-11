package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.NotificationMapper;
import cn.xidian.meetingroom.model.Notification;
import cn.xidian.meetingroom.model.NotificationExample;
import cn.xidian.meetingroom.service.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Notification getNotificationById(Integer notificationId) {
        return notificationMapper.selectByPrimaryKey(notificationId);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Integer userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return notificationMapper.selectByExample(example);
    }

    @Override
    public List<Notification> getUnreadNotifications(Integer userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
            .andUserIdEqualTo(userId.intValue())
            .andIsReadEqualTo(false);
        return notificationMapper.selectByExample(example);
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreatedTime(LocalDateTime.now());
        notification.setIsRead(false);
        notificationMapper.insertSelective(notification);
        return notification;
    }

    @Override
    public void markAsRead(Integer notificationId) {
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setIsRead(true);
        notification.setUpdatedTime(LocalDateTime.now());
        notificationMapper.updateByPrimaryKeySelective(notification);
    }

    @Override
    public void markAllAsRead(Integer userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
            .andUserIdEqualTo(userId.intValue())
            .andIsReadEqualTo(false);
        
        Notification notification = new Notification();
        notification.setIsRead(true);
        notification.setUpdatedTime(LocalDateTime.now());
        
        notificationMapper.updateByExampleSelective(notification, example);
    }

    @Override
    public void deleteNotification(Integer notificationId) {
        notificationMapper.deleteByPrimaryKey(notificationId);
    }

    // Utility method to create different types of notifications
    public void createSystemNotification(Integer userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("SYSTEM");
        notification.setContent(content);
        createNotification(notification);
    }

    public void createReservationNotification(Integer userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("RESERVATION");
        notification.setContent(content);
        createNotification(notification);
    }

    public void createMeetingRoomNotification(Integer userId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("MEETING_ROOM");
        notification.setContent(content);
        createNotification(notification);
    }
} 