package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.StatusResponse;
import io.github.pluton33.ezgloszenie.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatusController {
    @Autowired
    private StatusService service;

    @GetMapping("statusReport/{reportId}")
    public StatusResponse getStatusReport(@PathVariable Long reportId) {
        return service.getStatusReport(reportId);
    }

    @PutMapping("updateStatus/{reportId}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long reportId, @RequestBody String status) {
        service.updateStatus(reportId, status);
        return ResponseEntity.noContent().build();
    }
}
