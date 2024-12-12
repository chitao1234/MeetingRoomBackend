package cn.xidian.meetingroom.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import cn.xidian.meetingroom.security.CustomUserDetails;

public abstract class BaseController {
    
    /**
     * Get the current authenticated user's username
     * @return username of the current user, or null if not authenticated
     */
    protected String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }

    /**
     * Get the current authenticated user's ID
     * @return user ID of the current user, or null if not authenticated
     */
    protected Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
} 