package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.User;
import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
    void updateLastLoginTime(Long userId);
} 
