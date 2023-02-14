package com.piotr.springboot.hotelapp.domain.reservation;

import com.piotr.springboot.hotelapp.domain.guest.Guest;
import com.piotr.springboot.hotelapp.domain.room.Room;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    private Guest guest;
    @OneToOne
    private Room room;
    @Column(name = "from_date")
    private LocalDateTime from;
    @Column(name = "to_date")
    private LocalDateTime to;

    public Reservation() {
    }

    public Reservation(Long id, Guest guest, Room room, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
