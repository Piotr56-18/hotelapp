package com.piotr.springboot.hotelapp.domain.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping("/list")
    public String listRooms(Model model){
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "list-rooms";
    }
}
