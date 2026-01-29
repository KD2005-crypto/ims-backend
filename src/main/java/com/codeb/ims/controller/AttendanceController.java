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
@CrossOrigin(origins = "*", allowedHeaders = "*") // ✅ FIXES THE RED ERROR
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // 1. CHECK IN
    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        try {
            Attendance attendance = attendanceService.checkIn(email);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. CHECK OUT
    @PutMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        try {
            Attendance attendance = attendanceService.checkOut(email);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. GET HISTORY (For the Profile Page)
    @GetMapping("/{email}")
    public ResponseEntity<List<Attendance>> getHistory(@PathVariable String email) {
        return ResponseEntity.ok(attendanceService.getHistory(email));
    }
}