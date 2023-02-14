package com.piotr.springboot.hotelapp.domain.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @PostMapping("/save")
    public String saveGuest(@ModelAttribute("guest") Guest guest){
        guestService.save(guest);
        return "redirect:/guests/list";
    }
}
