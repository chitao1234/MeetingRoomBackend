package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NotificationMapper {
    Notification selectNotificationById(Long notificationId);
    List<Notification> selectNotificationsByUserId(Long userId);
    List<Notification> selectUnreadNotificationsByUserId(Long userId);
    void insertNotification(Notification notification);
    void updateNotification(Notification notification);
    void deleteNotification(Long notificationId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
}