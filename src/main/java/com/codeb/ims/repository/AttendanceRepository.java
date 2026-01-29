package com.codeb.ims.repository;

import com.codeb.ims.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;      // ✅ ADDED THIS IMPORT
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ✅ THIS WAS MISSING AND CAUSED THE ERROR
    // Used by Admin Panel to see everyone present today
    List<Attendance> findByDate(LocalDate date);

    // Used by Check-In logic to see if YOU are already here
    Optional<Attendance> findByEmailAndDate(String email, LocalDate date);
}