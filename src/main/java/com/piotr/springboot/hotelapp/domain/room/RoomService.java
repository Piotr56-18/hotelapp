package com.piotr.springboot.hotelapp.domain.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public List<Room> findAll(){
        return roomRepository.findAll();
    }
    public Room findById(Long id){
        Optional<Room> result = roomRepository.findById(id);
        Room room = null;
        if(result.isPresent()){
            room = result.get();
            return room;
        }else{
            throw new RuntimeException("Don't find room id: " + id);
        }
    }
    public void save (Room room){
        roomRepository.save(room);
    }
    public void deleteById(Long id){
        roomRepository.deleteById(id);
    }
    public Room findByNumber(int number){
        return roomRepository.findByNumber(number);
    }
}
