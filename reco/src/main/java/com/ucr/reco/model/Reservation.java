package com.ucr.reco.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "tb-reservations")
public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
    private LocalDateTime startDay;
    private LocalDateTime endDay;
    private Status status;

    public Reservation()
    {
    }

    public Reservation(Integer id, Space space, User user, LocalDateTime startDay, LocalDateTime endDay, Status status) {
        this.id = id;
        this.space = space;
        this.user = user;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDateTime startDay) {
        this.startDay = startDay;
    }

    public LocalDateTime getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDateTime endDay) {
        this.endDay = endDay;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


