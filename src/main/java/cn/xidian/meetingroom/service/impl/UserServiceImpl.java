package cn.xidian.meetingroom.service.impl;

import cn.xidian.meetingroom.mapper.UserMapper;
import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.model.UserExample;
import cn.xidian.meetingroom.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectByExample(new UserExample());
    }

    @Override
    public User createUser(User user) {
        // Check username existence
        UserExample usernameExample = new UserExample();
        usernameExample.createCriteria().andUsernameEqualTo(user.getUsername());
        if (!userMapper.selectByExample(usernameExample).isEmpty()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check email existence
        if (user.getEmail() != null) {
            UserExample emailExample = new UserExample();
            emailExample.createCriteria().andEmailEqualTo(user.getEmail());
            if (!userMapper.selectByExample(emailExample).isEmpty()) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedTime(LocalDateTime.now());
        userMapper.insertSelective(user);
        return user;
    }

    @Override
    public User updateUser(Integer userId, User user) {
        User existingUser = userMapper.selectByPrimaryKey(userId);
        if (existingUser == null) {
            return null;
        }

        user.setUserId(userId);
        user.setPassword(existingUser.getPassword());
        user.setCreatedTime(existingUser.getCreatedTime());
        userMapper.updateByPrimaryKeySelective(user);
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void deleteUser(Integer userId) {
        userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void updateLastLoginTime(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> getAllAdmins() {
        UserExample example = new UserExample();
        example.createCriteria().andRoleEqualTo("ADMIN");
        return userMapper.selectByExample(example);
    }

    @Override
    public void updateUserPassword(Integer userId, String newPassword) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Boolean checkPassword(Integer userId, String password) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }
} 