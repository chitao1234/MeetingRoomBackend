package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Date;

@Mapper
public interface UserMapper {
    User selectUserById(Long userId);
    User selectUserByUsername(String username);
    List<User> selectAllUsers();
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(Long userId);
    void updateLastLoginTime(Long userId, Date lastLoginTime);
    List<User> selectUsersByRole(String role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
