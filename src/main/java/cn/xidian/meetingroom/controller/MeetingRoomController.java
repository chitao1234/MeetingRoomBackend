package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.MeetingRoomWithBLOBs;
import cn.xidian.meetingroom.service.MeetingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    public MeetingRoomController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @GetMapping
    public ResponseEntity<List<MeetingRoomWithBLOBs>> getAllMeetingRooms() {
        List<MeetingRoomWithBLOBs> meetingRooms = meetingRoomService.getAllMeetingRooms();
        return ResponseEntity.ok(meetingRooms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MeetingRoomWithBLOBs>> searchMeetingRooms(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<MeetingRoomWithBLOBs> availableRooms = meetingRoomService.searchAvailableMeetingRooms(minCapacity, startTime, endTime);
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
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable("id") Integer meetingRoomId) {
        meetingRoomService.deleteMeetingRoom(meetingRoomId);
        return ResponseEntity.noContent().build();
    }
} 