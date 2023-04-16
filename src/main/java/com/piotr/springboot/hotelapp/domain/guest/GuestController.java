package com.piotr.springboot.hotelapp.domain.guest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/guests")
public class GuestController {
    private GuestService guestService;
    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/list")
    public String listGuests(Model model){
        List<Guest> guests = guestService.findAll();
        model.addAttribute("guests", guests);
        return "list-guests";
    }
    @GetMapping("/addGuestForm")
    public String addGuestForm(Model model){
        Guest guest = new Guest();
        model.addAttribute("guest", guest);
        return "guest-form";
    }
    @GetMapping("/updateGuestForm")
    public String aupdateGuestForm(@RequestParam("guestId") Long id,  Model model){
        Guest guest = guestService.findById(id);
        model.addAttribute("guest", guest);
        return "guest-form";
    }
    @PostMapping("/save")
    public String saveGuest(@ModelAttribute("guest") @Valid Guest guest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "guest-form";
        }
        guestService.save(guest);
        return "redirect:/guests/list";
    }
    @GetMapping("/delete")
    public String deleteGuest(@RequestParam("guestId") Long id){
        guestService.deleteById(id);
        return "redirect:/guests/list";
    }
}
