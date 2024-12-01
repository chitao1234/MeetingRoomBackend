package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.User;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getUserById(Long userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long userId, User user);
    void deleteUser(Long userId);
    void updateLastLoginTime(Long userId);
} 
