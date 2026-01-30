package com.codeb.ims.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDate date;
    private LocalTime checkInTime; // Stored as LocalTime in DB
    private LocalTime checkOutTime;
    private String status;

    // ✅ ADD THIS NEW FIELD
    private String workHours;

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }
    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ✅ ADD THESE GETTER/SETTER
    public String getWorkHours() { return workHours; }
    public void setWorkHours(String workHours) { this.workHours = workHours; }
}