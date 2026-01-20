package com.codeb.ims.controller;

import com.codeb.ims.dto.DashboardStats;
import com.codeb.ims.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping
    public DashboardStats getStats() {
        return service.getStats();
    }
}