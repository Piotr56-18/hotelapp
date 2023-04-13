package com.piotr.springboot.hotelapp.domain.room;

import com.piotr.springboot.hotelapp.domain.guest.Guest;
import com.piotr.springboot.hotelapp.util.RoomNumberValidator;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String saveRoom(@ModelAttribute("room") @Valid Room room, BindingResult bindingResult){
        if(Optional.ofNullable(roomService.findByNumber(room.getNumber())).isPresent()){
         bindingResult.rejectValue("number", "error.number","Room number already exist!");
         return "room-form";
        }
        if(bindingResult.hasErrors()){
            return "room-form";
        }
        roomService.save(room);
        return "redirect:/rooms/list";
    }
    @PostMapping(path = "/save",params = "addBed")
    public String addBed(@ModelAttribute("room") Room room){
        if(room.getBeds() == null){
            room.setBeds(new ArrayList<>());
        }
        room.getBeds().add(BedType.SINGLE);
        return "room-form";
    }
    @GetMapping("/delete")
    public String deleteRoom(@RequestParam("roomId") Long id){
        roomService.deleteById(id);
        return "redirect:/rooms/list";
    }
}
