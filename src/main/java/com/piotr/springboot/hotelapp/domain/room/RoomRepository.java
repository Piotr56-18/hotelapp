package com.piotr.springboot.hotelapp.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    public Room findByNumber(int number);
}
