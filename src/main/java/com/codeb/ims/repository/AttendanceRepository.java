package com.codeb.ims.repository;

import com.codeb.ims.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Find the latest record for this user today
    Optional<Attendance> findByEmailAndDate(String email, LocalDate date);

    // Get all history for this user
    List<Attendance> findAllByEmailOrderByDateDesc(String email);
}