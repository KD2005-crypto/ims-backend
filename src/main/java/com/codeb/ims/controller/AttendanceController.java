package com.codeb.ims.controller;

import com.codeb.ims.entity.Attendance;
import com.codeb.ims.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, String> payload) {
        try {
            return ResponseEntity.ok(attendanceService.checkIn(payload.get("email")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody Map<String, String> payload) {
        try {
            return ResponseEntity.ok(attendanceService.checkOut(payload.get("email")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<Attendance>> getHistory(@PathVariable String email) {
        return ResponseEntity.ok(attendanceService.getHistory(email));
    }

    // ✅ ADD THIS ENDPOINT (This is what the Admin Dashboard needs!)
    @GetMapping("/today")
    public ResponseEntity<List<Attendance>> getToday() {
        return ResponseEntity.ok(attendanceService.getAllToday());
    }
}