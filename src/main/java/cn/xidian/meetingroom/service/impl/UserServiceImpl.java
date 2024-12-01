package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.UserMapper;
import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public User createUser(User user) {
        // Set default values
        user.setCreatedTime(new Date());
        // You might want to add password hashing here
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null) {
            return null;
        }

        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setCreatedTime(existingUser.getCreatedTime());
        updateUser.setPassword(existingUser.getPassword()); // Don't update password in basic update
        
        if (user.getUsername() != null) {
            updateUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            updateUser.setPhone(user.getPhone());
        }
        if (user.getRole() != null) {
            updateUser.setRole(user.getRole());
        }
        
        userMapper.updateUser(updateUser);
        return userMapper.selectUserById(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userMapper.deleteUser(userId);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        userMapper.updateLastLoginTime(userId);
    }
} 