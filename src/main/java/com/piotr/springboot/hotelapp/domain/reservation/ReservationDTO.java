package com.piotr.springboot.hotelapp.domain.reservation;

import com.piotr.springboot.hotelapp.domain.guest.GuestDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReservationDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    private Long roomId;
    private GuestDTO guestDTO;

    public ReservationDTO(LocalDate from, LocalDate to, Long roomId) {
        this.from = from;
        this.to = to;
        this.roomId = roomId;
    }
    public GuestDTO getGuestDTO() {
        return guestDTO;
    }

    public void setGuestDTO(GuestDTO guestDTO) {
        this.guestDTO = guestDTO;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
