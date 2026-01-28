package com.codeb.ims.repository;

import com.codeb.ims.entity.Attendance; // Make sure you have an Attendance entity!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find attendance by Email and Date (to check if already checked in)
    Optional<Attendance> findByEmailAndDate(String email, LocalDate date);

    // Optional: Delete all records (if you want a hard reset at 5 AM)
    // void deleteAll();
}