package com.example.lab4.controller;

import com.example.lab4.model.Point;
import com.example.lab4.repository.PointRepo;
import com.example.lab4.request.PointRequestDTO;
import com.example.lab4.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/point")
public class PointController {

    private final PointService service;

    @Autowired
    public PointController(PointService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Point> newPoint(final Principal principal, @RequestBody PointRequestDTO rq) {
        return ResponseEntity.ok(Point.toModel(service.newPoint(principal.getName(), rq)));
    }

    @GetMapping
    public ResponseEntity<List<Point>> allResultsByUser(Principal principal) {
        return ResponseEntity.ok(service.getAllByUsername(principal));
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity<?> deleteAll(Principal principal) {
        service.removeAllFromUser(principal);
        return ResponseEntity.noContent().build();
    }
}
