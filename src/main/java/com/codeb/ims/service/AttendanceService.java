package com.codeb.ims.service;

import com.codeb.ims.entity.Attendance;
import com.codeb.ims.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    // ✅ FORCE IST TIMEZONE (Fixes the wrong time issue)
    private final ZoneId IST = ZoneId.of("Asia/Kolkata");

    public Attendance checkIn(String email) {
        LocalDate today = LocalDate.now(IST);

        // Prevent double check-in
        if (repository.findByEmailAndDate(email, today).isPresent()) {
            throw new RuntimeException("You have already checked in today!");
        }

        Attendance attendance = new Attendance();
        attendance.setEmail(email);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalTime.now(IST)); // Use IST Time
        attendance.setStatus("PRESENT");
        attendance.setWorkHours("Active"); // Default text until checkout
        return repository.save(attendance);
    }

    public Attendance checkOut(String email) {
        LocalDate today = LocalDate.now(IST);

        Attendance attendance = repository.findByEmailAndDate(email, today)
                .orElseThrow(() -> new RuntimeException("You haven't checked in yet!"));

        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("You have already checked out!");
        }

        LocalTime now = LocalTime.now(IST); // Use IST Time
        attendance.setCheckOutTime(now);
        attendance.setStatus("COMPLETED");

        // ✅ CALCULATE WORK HOURS
        try {
            Duration duration = Duration.between(attendance.getCheckInTime(), now);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            String hoursWorked = String.format("%dh %dm", hours, minutes); // e.g., "8h 15m"
            attendance.setWorkHours(hoursWorked);
        } catch (Exception e) {
            attendance.setWorkHours("Error");
        }

        return repository.save(attendance);
    }

    public List<Attendance> getHistory(String email) {
        return repository.findAllByEmailOrderByDateDesc(email);
    }

    public List<Attendance> getAllToday() {
        return repository.findAllByDate(LocalDate.now(IST));
    }
}