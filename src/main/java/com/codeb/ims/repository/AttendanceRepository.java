package com.codeb.ims.repository;

import com.codeb.ims.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmailAndDate(String email, LocalDate date);

    List<Attendance> findAllByEmailOrderByDateDesc(String email);

    // Used for the Admin Dashboard (Daily Log)
    List<Attendance> findAllByDate(LocalDate date);

    // ✅ CRITICAL NEW METHOD: Used by the Scheduler
    // Finds everyone who is still "PRESENT" at 11:59 PM
    List<Attendance> findByDateAndStatus(LocalDate date, String status);
}