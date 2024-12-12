package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Notification;
import cn.xidian.meetingroom.model.NotificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NotificationMapper {
    long countByExample(NotificationExample example);

    int deleteByExample(NotificationExample example);

    int deleteByPrimaryKey(Long notificationId);

    int insert(Notification row);

    int insertSelective(Notification row);

    List<Notification> selectByExampleWithBLOBs(NotificationExample example);

    List<Notification> selectByExample(NotificationExample example);

    Notification selectByPrimaryKey(Long notificationId);

    int updateByExampleSelective(@Param("row") Notification row, @Param("example") NotificationExample example);

    int updateByExampleWithBLOBs(@Param("row") Notification row, @Param("example") NotificationExample example);

    int updateByExample(@Param("row") Notification row, @Param("example") NotificationExample example);

    int updateByPrimaryKeySelective(Notification row);

    int updateByPrimaryKeyWithBLOBs(Notification row);

    int updateByPrimaryKey(Notification row);
}