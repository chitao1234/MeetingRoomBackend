package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    Notification selectNotificationById(int notificationId);
    // Additional CRUD methods
}
