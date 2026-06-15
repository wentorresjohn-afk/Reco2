package com.ucr.reco.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class ReservationDTO
{
    @NotNull(message = "El espacio es obligatorio")
    private Integer spaceId;// ID del espacio

    @NotNull(message = "El espacio es obligatorio")
    private String userEmail;// ID del usuario

    @NotNull(message = "La fecha de inicio es obligatoria")
    //@Future(message = "La fecha de inicio debe ser futura")
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDay;// Fecha y hora de inicio

    @NotNull(message = "La fecha de fin es obligatoria")
    //@Future(message = "La fecha de fin debe ser futura")
    @JsonFormat (pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endDay;// Fecha y hora de fin

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDING|CONFIRMED|CANCELED", message = "Estado inválido")
    private String status;// "PENDING", "CONFIRMED" o "CANCELED

    public ReservationDTO()
    {
    }

    public ReservationDTO(Integer spaceId, String userEmail, LocalDateTime startDay, LocalDateTime endDay, String status) {
        this.spaceId = spaceId;
        this.userEmail = userEmail;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
