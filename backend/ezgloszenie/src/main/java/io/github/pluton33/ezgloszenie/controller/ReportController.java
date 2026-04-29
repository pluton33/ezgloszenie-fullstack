package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.Report;
import io.github.pluton33.ezgloszenie.data.ReportsResponse;
import io.github.pluton33.ezgloszenie.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    @PreAuthorize("isAuthenticated()")
    public Report addReport(@RequestBody Report report, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        return service.addReport(report, email);
    }

    @PutMapping("reports/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Report editReport(@PathVariable int id, @RequestBody Report report, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        return service.editReport(id, report, email);
    }

    @DeleteMapping("reports/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReport(@PathVariable int id, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        service.deleteReport(id, email);
        return ResponseEntity.noContent().build();
    }
}
