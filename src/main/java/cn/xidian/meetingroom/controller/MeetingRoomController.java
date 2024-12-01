package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.service.MeetingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    public MeetingRoomController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @GetMapping
    public ResponseEntity<List<MeetingRoom>> getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomService.getAllMeetingRooms();
        return ResponseEntity.ok(meetingRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable("id") Long meetingRoomId) {
        MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomById(meetingRoomId);
        if (meetingRoom == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetingRoom);
    }

    @PostMapping
    public ResponseEntity<MeetingRoom> createMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        MeetingRoom createdRoom = meetingRoomService.createMeetingRoom(meetingRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeetingRoom> updateMeetingRoom(
            @PathVariable("id") Long meetingRoomId,
            @RequestBody MeetingRoom meetingRoom) {
        MeetingRoom updatedRoom = meetingRoomService.updateMeetingRoom(meetingRoomId, meetingRoom);
        if (updatedRoom == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable("id") Long meetingRoomId) {
        meetingRoomService.deleteMeetingRoom(meetingRoomId);
        return ResponseEntity.noContent().build();
    }
} 