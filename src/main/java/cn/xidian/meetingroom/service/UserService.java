package cn.xidian.meetingroom.service;

import cn.xidian.meetingroom.model.User;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getUserById(Integer userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Integer userId, User user);
    void deleteUser(Integer userId);
    void updateLastLoginTime(Integer userId);
} 
