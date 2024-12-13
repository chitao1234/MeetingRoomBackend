package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.User;
import java.util.List;

public interface UserService {
    User getUserById(Integer userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Integer userId, User user);
    void deleteUser(Integer userId);
    void updateLastLoginTime(Integer userId);
    List<User> getAllAdmins();
    void updateUserPassword(Integer userId, String newPassword);
    Boolean checkPassword(Integer userId, String password);
} 
