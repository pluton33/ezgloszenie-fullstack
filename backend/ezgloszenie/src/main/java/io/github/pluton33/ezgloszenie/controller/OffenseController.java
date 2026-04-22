package io.github.pluton33.ezgloszenie.controller;

import io.github.pluton33.ezgloszenie.data.Offense;
import io.github.pluton33.ezgloszenie.data.OffensesResponse;
import io.github.pluton33.ezgloszenie.service.OffenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OffenseController {
    @Autowired
    private OffenseService service;

    @GetMapping("/offenses")
    public OffensesResponse getOffenses() {
        return service.getOffenses();
    }

    @GetMapping("offenses/{id}")
    public Offense getOffenseById(@PathVariable int id) {
        return service.getOffenseById(id);
    }

    @PostMapping("addOffense")
    @ResponseStatus(HttpStatus.CREATED)
    public Offense addOffense(@RequestBody Offense offense) {
        return service.addOffense(offense);
    }

    @PostMapping("editOffense")
    @ResponseStatus(HttpStatus.CREATED)
    public Offense editOffense(@RequestBody Offense offense) {
        return service.editOffense(offense);
    }

    @DeleteMapping("offenses/{id}")
    public ResponseEntity<Void> deleteOffense(@PathVariable int id) {
        service.deleteOffense(id);
        return ResponseEntity.noContent().build();
    }
}
