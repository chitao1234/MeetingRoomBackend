package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;
import cn.xidian.meetingroom.service.MeetingRoomService;
import cn.xidian.meetingroom.service.LogService;
import cn.xidian.meetingroom.util.IpUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController extends BaseController {

    private final MeetingRoomService meetingRoomService;
    private final LogService logService;
    private final HttpServletRequest request;

    public MeetingRoomController(MeetingRoomService meetingRoomService,
                               LogService logService,
                               HttpServletRequest request) {
        this.meetingRoomService = meetingRoomService;
        this.logService = logService;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<MeetingRoomWithBLOBs>> searchMeetingRooms(
            @RequestParam(required = false) Integer attendees,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<MeetingRoomWithBLOBs> availableRooms = meetingRoomService.searchAvailableMeetingRooms(attendees, startTime, endTime);
        return ResponseEntity.ok(availableRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoomWithBLOBs> getMeetingRoom(@PathVariable("id") Integer meetingRoomId) {
        MeetingRoomWithBLOBs meetingRoom = meetingRoomService.getMeetingRoomById(meetingRoomId);
        if (meetingRoom == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingRoom);
    }

    @PostMapping
    public ResponseEntity<MeetingRoomWithBLOBs> createMeetingRoom(@RequestBody MeetingRoomWithBLOBs meetingRoom) {
        MeetingRoomWithBLOBs createdRoom = meetingRoomService.createMeetingRoom(meetingRoom);
        
        // Create audit log
        String details = String.format("Created new meeting room: %s", createdRoom.getName());
        logService.createLog(getCurrentUserId(), "CREATE_MEETING_ROOM", details, 
            IpUtil.getIpAddressBytes(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeetingRoomWithBLOBs> updateMeetingRoom(
            @PathVariable("id") Integer meetingRoomId,
            @RequestBody MeetingRoomWithBLOBs meetingRoom) {
        MeetingRoomWithBLOBs updatedRoom = meetingRoomService.updateMeetingRoom(meetingRoomId, meetingRoom);
        if (updatedRoom == null) {
            return ResponseEntity.notFound().build();
        }

        // Create audit log
        String details = String.format("Updated meeting room: %s", updatedRoom.getName());
        logService.createLog(getCurrentUserId(), "UPDATE_MEETING_ROOM", details, 
            IpUtil.getIpAddressBytes(request));

        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable("id") Integer meetingRoomId) {
        MeetingRoomWithBLOBs room = meetingRoomService.getMeetingRoomById(meetingRoomId);
        if (room != null) {
            // Create audit log before deletion
            String details = String.format("Deleted meeting room: %s", room.getName());
            logService.createLog(getCurrentUserId(), "DELETE_MEETING_ROOM", details, 
                IpUtil.getIpAddressBytes(request));
        }

        meetingRoomService.deleteMeetingRoom(meetingRoomId);
        return ResponseEntity.noContent().build();
    }
} 