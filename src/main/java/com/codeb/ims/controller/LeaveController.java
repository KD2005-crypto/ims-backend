package com.codeb.ims.controller;

import com.codeb.ims.entity.LeaveRequest;
import com.codeb.ims.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveRepository leaveRepository;

    // ✅ 1. USER PORTAL: Submit a new leave request
    @PostMapping("/apply")
    public ResponseEntity<LeaveRequest> applyLeave(@RequestBody LeaveRequest request) {
        // Ensure new requests always start as PENDING
        request.setStatus("PENDING");
        return ResponseEntity.ok(leaveRepository.save(request));
    }

    // ✅ 2. ADMIN PORTAL: Get all pending requests for the Approvals tab
    @GetMapping("/pending")
    public List<LeaveRequest> getPendingLeaves() {
        return leaveRepository.findByStatus("PENDING");
    }

    // ✅ 3. ADMIN PORTAL: Handle Approve/Reject buttons
    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        LeaveRequest request = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (status.equalsIgnoreCase("approve")) {
            request.setStatus("APPROVED");
        } else if (status.equalsIgnoreCase("reject")) {
            request.setStatus("REJECTED");
        }

        return ResponseEntity.ok(leaveRepository.save(request));
    }

    // ✅ NEW 4. USER PORTAL: Get all history for a specific user (Persistent Log)
    // This prevents requests from "vanishing" after they are approved/rejected
    @GetMapping("/user/{email}")
    public List<LeaveRequest> getUserLeaves(@PathVariable String email) {
        return leaveRepository.findByEmail(email);
    }

    // ✅ NEW 5. USER PORTAL: Delete a record to clear the inbox mess
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeave(@PathVariable Long id) {
        try {
            leaveRepository.deleteById(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not delete record");
        }
    }
}