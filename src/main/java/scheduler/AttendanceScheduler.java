package com.codeb.ims.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.codeb.ims.repository.AttendanceRepository; // Ensure you have this repository

@Component
public class AttendanceScheduler {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // ⏰ CRON JOB: Runs every day at 05:00 AM
    @Scheduled(cron = "0 0 5 * * ?")
    public void resetDailyAttendance() {
        System.out.println("🔄 [5:00 AM] System Maintenance: Resetting Attendance...");

        // Database logic to reset attendance
        // Example: attendanceRepository.deleteAll(); OR mark all as absent
        // This depends on how your database is set up.

        System.out.println("✅ [5:00 AM] Attendance Reset Complete.");
    }
}