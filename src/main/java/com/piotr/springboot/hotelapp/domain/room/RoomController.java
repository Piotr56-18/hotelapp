package com.piotr.springboot.hotelapp.domain.room;

import com.piotr.springboot.hotelapp.domain.guest.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/addRoomForm")
    public String addRoomForm(Model model){
        Room room = new Room();
        model.addAttribute("room", room);
        return "room-form";
    }
    @GetMapping("/updateRoomForm")
    public String aupdateRoomForm(@RequestParam("roomId") Long id,  Model model){
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        return "room-form";
    }
    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("room") Room room){
        roomService.save(room);
        return "redirect:/rooms/list";
    }
    @GetMapping("/delete")
    public String deleteRoom(@RequestParam("roomId") Long id){
        roomService.deleteById(id);
        return "redirect:/rooms/list";
    }
}
