package com.codeb.ims.repository;

import com.codeb.ims.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmailAndDate(String email, LocalDate date);
    List<Attendance> findAllByEmailOrderByDateDesc(String email);

    // ✅ ADD THIS LINE (Find everyone present on a specific date)
    List<Attendance> findAllByDate(LocalDate date);
}