package com.codeb.ims.scheduler;

import com.codeb.ims.entity.Attendance;
import com.codeb.ims.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AttendanceScheduler {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // ✅ FORCE IST TIMEZONE
    private final ZoneId IST = ZoneId.of("Asia/Kolkata");

    // ⏰ CRON JOB: Runs every day at 11:59 PM (23:59)
    // "0 59 23 * * ?" = At second 0, minute 59, hour 23, every day
    @Scheduled(cron = "0 59 23 * * ?", zone = "Asia/Kolkata")
    public void autoCheckoutPendingStaff() {
        System.out.println("🔄 [11:59 PM] System Maintenance: Checking for forgotten check-outs...");

        LocalDate today = LocalDate.now(IST);

        // 1. Find everyone who is still "PRESENT" (forgot to check out)
        List<Attendance> forgotToCheckout = attendanceRepository.findByDateAndStatus(today, "PRESENT");

        for (Attendance att : forgotToCheckout) {
            // 2. Force Check-out
            att.setCheckOutTime(LocalTime.of(23, 59)); // Set time to end of day
            att.setStatus("AUTO-OUT"); // Mark differently so you know they forgot
            att.setWorkHours("System Auto-Close"); // Flag for Admin to see

            attendanceRepository.save(att);
            System.out.println("⚠️ Auto-checked out: " + att.getEmail());
        }

        System.out.println("✅ Maintenance Complete. " + forgotToCheckout.size() + " records updated.");
    }
}