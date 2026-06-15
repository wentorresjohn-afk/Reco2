package com.ucr.reco.controller;

import com.ucr.reco.model.Reservation;
import com.ucr.reco.model.dto.ReservationDTO;
import com.ucr.reco.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController
{
    @Autowired
    private ReservationService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAll()
    {
       return ResponseEntity.ok(service.findAll());
    }//Fin del metodo

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ReservationDTO reservation)
    {
        return ResponseEntity.ok(service.add(reservation));
    }
}
