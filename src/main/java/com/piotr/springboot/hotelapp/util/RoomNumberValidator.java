package com.piotr.springboot.hotelapp.util;

import com.piotr.springboot.hotelapp.domain.room.Room;
import com.piotr.springboot.hotelapp.domain.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RoomNumberValidator implements Validator {
    @Autowired
    private RoomService roomService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Room.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Room room = (Room) target;

        if(roomService.findByNumber(room.getNumber())!=null){
            errors.rejectValue("number", "room.number.exists", "Room number already exists!");
        }

    }
}
