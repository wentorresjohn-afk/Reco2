package com.ucr.reco.service;

import com.ucr.reco.model.Reservation;
import com.ucr.reco.model.Space;
import com.ucr.reco.model.Status;
import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.ReservationDTO;
import com.ucr.reco.repository.ReservationJpaRepository;
import com.ucr.reco.repository.SpaceJpaRepository;
import com.ucr.reco.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService
{
    @Autowired
    private ReservationJpaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    public List<Reservation> findAll()
    {
        return repository.findAll();
    }//Fin del metodo

    public Reservation add(ReservationDTO reservation)
    {
        Space space = spaceService.getById(reservation.getSpaceId());
        User user = userService.getUserByEmail(reservation.getUserEmail());
        Reservation reservationTemp  = new Reservation();

        reservationTemp.setSpace(space);
        reservationTemp.setUser(user);
        reservationTemp.setStartDay(reservation.getStartDay());
        reservationTemp.setEndDay(reservation.getEndDay());
        reservationTemp.setStatus(Status.PENDING);

        return repository.save(reservationTemp);
    }
}

