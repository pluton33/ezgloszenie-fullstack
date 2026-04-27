package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportController {
    @Autowired
    private ReportService service;

    @GetMapping("/reports")
    public ReportsResponse getReports() {
        return service.getReports();
    }

    @GetMapping("reports/{id}")
    public Report getReportById(@PathVariable int id) {
        return service.getReportById(id);
    }

    @PostMapping("addReport")
    @ResponseStatus(HttpStatus.CREATED)
    public Report addReport(@RequestBody Report report) {
        return service.addReport(report);
    }

    @PutMapping("reports/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Report editReport(@PathVariable int id, @RequestBody Report report) {
        return service.editReport(id, report);
    }

    @DeleteMapping("reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable int id) {
        service.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
