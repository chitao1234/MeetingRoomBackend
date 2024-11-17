package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectUserById(int userId);
    // Additional CRUD methods
}
