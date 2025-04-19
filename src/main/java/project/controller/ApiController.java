package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.service.PowerService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PowerService powerService;

    @GetMapping(path = { "/", ""})
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok(powerService.getData("activePowerW"));
    }
}
