package com.piotr.springboot.hotelapp.domain.room;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/list/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model){
        int pageSize = 5;
        Page<Room>page = roomService.findPaginated(pageNo,pageSize, sortField,sortDir);
        List<Room>rooms = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");

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
    public String updateRoomForm(@RequestParam("roomId") Long id,  Model model){
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        return "update-room-form";
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
    @PostMapping("/update")
    public String updateRoom(@ModelAttribute("room") @Valid Room room, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "update-room-form";
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
    @PostMapping(path = "/update",params = "addBed")
    public String addBedUpdate(@ModelAttribute("room") Room room){
        room.getBeds().add(BedType.SINGLE);
        return "update-room-form";
    }
    @GetMapping("/delete")
    public String deleteRoom(@RequestParam("roomId") Long id){
        roomService.deleteById(id);
        return "redirect:/rooms/list";
    }
    @GetMapping("/list")
    public String listRooms(Model model){
        return findPaginated(1,"number", "asc", model);
    }
}
