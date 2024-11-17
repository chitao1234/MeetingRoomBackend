package cn.xidian.meetingroom.controller;

import cn.xidian.meetingroom.model.MeetingRoom;
import cn.xidian.meetingroom.service.MeetingRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    public MeetingRoomController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable("id") int meetingRoomId) {
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
} 