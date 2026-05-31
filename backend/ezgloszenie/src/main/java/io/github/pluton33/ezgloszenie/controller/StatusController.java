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

    @GetMapping("statusReport/{report_id}")
    public StatusResponse getStatusReport(@PathVariable Long report_id) {
        return service.getStatusReport(report_id);
    }

    @PutMapping("updateStatus/{report_id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long report_id, @RequestBody String status) {
        service.updateStatus(report_id, status);
        return ResponseEntity.noContent().build();
    }
}
