package com.codeb.ims.service;

import com.codeb.ims.entity.Attendance;
import com.codeb.ims.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    public Attendance checkIn(String email) {
        LocalDate today = LocalDate.now();

        // Prevent double check-in
        if (repository.findByEmailAndDate(email, today).isPresent()) {
            throw new RuntimeException("You have already checked in today!");
        }

        Attendance attendance = new Attendance();
        attendance.setEmail(email);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalTime.now());
        attendance.setStatus("PRESENT");

        return repository.save(attendance);
    }

    public Attendance checkOut(String email) {
        LocalDate today = LocalDate.now();

        Attendance attendance = repository.findByEmailAndDate(email, today)
                .orElseThrow(() -> new RuntimeException("You haven't checked in yet!"));

        attendance.setCheckOutTime(LocalTime.now());
        return repository.save(attendance);
    }

    public List<Attendance> getHistory(String email) {
        return repository.findAllByEmailOrderByDateDesc(email);
    }
}