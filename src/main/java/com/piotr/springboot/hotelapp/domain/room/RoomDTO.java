package com.piotr.springboot.hotelapp.domain.room;

public class RoomDTO {
    private Long id;

    public RoomDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
