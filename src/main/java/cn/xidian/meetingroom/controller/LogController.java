package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.LogWithBLOBs;
import cn.xidian.meetingroom.service.LogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@PreAuthorize("hasRole('ADMIN')")  // Ensure only admins can access these endpoints
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<LogWithBLOBs>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @GetMapping("/{logId}")
    public ResponseEntity<LogWithBLOBs> getLogById(@PathVariable Long logId) {
        LogWithBLOBs log = logService.getLogById(logId);
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(log);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LogWithBLOBs>> getLogsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(logService.getLogsByUserId(userId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<LogWithBLOBs>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(logService.getLogsByDateRange(startDate, endDate));
    }

    @GetMapping("/operation-type/{operationType}")
    public ResponseEntity<List<LogWithBLOBs>> getLogsByOperationType(@PathVariable String operationType) {
        return ResponseEntity.ok(logService.getLogsByOperationType(operationType));
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanupOldLogs(@RequestParam(defaultValue = "30") int daysToKeep) {
        logService.deleteOldLogs(daysToKeep);
        return ResponseEntity.ok().build();
    }
} 