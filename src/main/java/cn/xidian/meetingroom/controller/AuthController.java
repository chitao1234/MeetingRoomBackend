package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.security.CustomUserDetailsService;
import cn.xidian.meetingroom.security.JwtUtils;
import cn.xidian.meetingroom.service.UserService;
import cn.xidian.meetingroom.service.LogService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final LogService logService;
    private final HttpServletRequest request;

    public AuthController(AuthenticationManager authenticationManager, 
                         CustomUserDetailsService userDetailsService, 
                         UserService userService,
                         JwtUtils jwtUtils,
                         LogService logService,
                         HttpServletRequest request) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.logService = logService;
        this.request = request;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        
        User user = userService.getUserByUsername(request.getUsername());
        userService.updateLastLoginTime(user.getUserId());

        // Create audit log
        String details = String.format("User %s logged in successfully", user.getUsername());
        logService.createLog(user.getUserId(), "USER_LOGIN", details, 
            this.request.getRemoteAddr().getBytes());
        
        return ResponseEntity.ok(new AuthResponse(jwt, user.getUserId(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("USER");
        
        User createdUser = userService.createUser(user);
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(createdUser.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        
        // Create audit log
        String details = String.format("New user registered: %s", createdUser.getUsername());
        logService.createLog(createdUser.getUserId(), "USER_REGISTER", details, 
            this.request.getRemoteAddr().getBytes());

        return ResponseEntity.ok(new AuthResponse(jwt, createdUser.getUserId(), createdUser.getRole()));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenValidationRequest request) {
        String username = jwtUtils.extractUsername(request.getToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        boolean isValid = jwtUtils.isTokenValid(request.getToken(), userDetails);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtils.extractUsername(token);
            User user = userService.getUserByUsername(username);
            
            jwtUtils.invalidateToken(token);

            // Create audit log
            String details = String.format("User %s logged out", username);
            logService.createLog(user.getUserId(), "USER_LOGOUT", details, 
                this.request.getRemoteAddr().getBytes());

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/check-admin")
    public ResponseEntity<Boolean> checkAdmin(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtils.extractUsername(token);
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok("ADMIN".equals(user.getRole()));
        }
        return ResponseEntity.ok(false);
    }
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
}

@Data
class TokenValidationRequest {
    private String token;
}

@Data
class AuthResponse {
    private String token;
    private Integer userId;
    private String role;
    
    public AuthResponse(String token, Integer userId, String role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }
} 