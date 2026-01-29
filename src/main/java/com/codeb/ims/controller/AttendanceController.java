package com.codeb.ims.controller;

import com.codeb.ims.entity.Attendance;
import com.codeb.ims.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin("*") // Allow React to access this
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // 1. CHECK IN ENDPOINT
    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String name = payload.get("name");
        LocalDate today = LocalDate.now();

        // Prevent double check-in
        Optional<Attendance> existing = attendanceRepository.findByEmailAndDate(email, today);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Already checked in for today!");
        }

        Attendance att = new Attendance();
        att.setEmail(email);
        att.setName(name);
        att.setDate(today);
        att.setCheckInTime(LocalTime.now());
        att.setStatus("Present");

        attendanceRepository.save(att);
        return ResponseEntity.ok("Checked In Successfully");
    }

    // 2. CHECK OUT ENDPOINT
    @PutMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        LocalDate today = LocalDate.now();

        Optional<Attendance> existing = attendanceRepository.findByEmailAndDate(email, today);
        if (existing.isPresent()) {
            Attendance att = existing.get();
            att.setCheckOutTime(LocalTime.now());
            attendanceRepository.save(att);
            return ResponseEntity.ok("Checked Out Successfully");
        }
        return ResponseEntity.badRequest().body("Record not found");
    }

    // 3. GET TODAY'S LIST (For Admin)
    @GetMapping("/today")
    public List<Attendance> getTodayAttendance() {
        return attendanceRepository.findByDate(LocalDate.now());
    }
}