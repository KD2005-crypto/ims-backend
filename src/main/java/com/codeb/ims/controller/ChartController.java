package com.codeb.ims.controller;

import com.codeb.ims.entity.ChartData;
import com.codeb.ims.repository.ChartDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charts")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class ChartController {

    @Autowired
    private ChartDataRepository chartRepository;

    @GetMapping
    public List<ChartData> getAllCharts() {
        return chartRepository.findAll();
    }

    @PutMapping("/{name}")
    public ResponseEntity<ChartData> updateChart(@PathVariable String name, @RequestBody ChartData incoming) {
        return chartRepository.findByName(name)
                .map(chart -> {
                    chart.setLabels(incoming.getLabels());
                    chart.setDataPoints(incoming.getDataPoints());
                    chart.setDescription(incoming.getDescription());
                    chart.setUpdateTime("just updated");
                    return ResponseEntity.ok(chartRepository.save(chart));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}