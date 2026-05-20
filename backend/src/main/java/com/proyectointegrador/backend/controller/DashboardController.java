package com.proyectointegrador.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyectointegrador.backend.service.DashboardService;
import com.proyectointegrador.backend.dto.DashboardDTO;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardDTO obtenerDatosDashboard() {
        return dashboardService.obtenerDatosDashboard();
    }
}
