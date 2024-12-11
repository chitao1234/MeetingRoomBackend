package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.User;
import cn.xidian.meetingroom.security.JwtUtils;
import cn.xidian.meetingroom.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, 
                         UserService userService, 
                         JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        
        User user = userService.getUserByUsername(request.getUsername());
        userService.updateLastLoginTime(user.getUserId());
        
        return ResponseEntity.ok(new AuthResponse(jwt, user.getUserId()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("USER");  // Default role for new registrations
        
        User createdUser = userService.createUser(user);
        
        final UserDetails userDetails = userService.loadUserByUsername(createdUser.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        
        return ResponseEntity.ok(new AuthResponse(jwt, createdUser.getUserId()));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenValidationRequest request) {
        String username = jwtUtils.extractUsername(request.getToken());
        UserDetails userDetails = userService.loadUserByUsername(username);
        
        boolean isValid = jwtUtils.isTokenValid(request.getToken(), userDetails);
        return ResponseEntity.ok(isValid);
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
    
    public AuthResponse(String token, Integer userId) {
        this.token = token;
        this.userId = userId;
    }
} 