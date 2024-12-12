package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.service.UserService;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final LogService logService;
    private final HttpServletRequest request;

    public UserController(UserService userService, LogService logService, HttpServletRequest request) {
        this.userService = userService;
        this.logService = logService;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Integer userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        
        // Create audit log
        String details = String.format("Created new user: %s with role %s", 
            createdUser.getUsername(), createdUser.getRole());
        logService.createLog(getCurrentUserId(), "CREATE_USER", details, 
            request.getRemoteAddr().getBytes());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Create audit log
        String details = String.format("Updated user: %s", updatedUser.getUsername());
        logService.createLog(getCurrentUserId(), "UPDATE_USER", details, 
            request.getRemoteAddr().getBytes());

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            // Create audit log before deletion
            String details = String.format("Deleted user: %s", user.getUsername());
            logService.createLog(getCurrentUserId(), "DELETE_USER", details, 
                request.getRemoteAddr().getBytes());
        }
        
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
} 