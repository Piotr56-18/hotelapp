package com.piotr.springboot.hotelapp.domain.guest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/guests")
public class GuestController {
    private GuestService guestService;
    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }
    @GetMapping("/list/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model){
        int pageSize = 4;
        Page<Guest> page = guestService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Guest>guests = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");

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
    @GetMapping("/list")
    public String listGuests(Model model){
        return findPaginated(1, "lastName", "asc", model);//default sorting
    }
    /* Without pagination
    @GetMapping("/list")
    public String listGuests(Model model){
        List<Guest> guests = guestService.findAll();
        model.addAttribute("guests", guests);
        return "list-guests";
    }
     // Without soring
    @GetMapping("/list")
    public String listGuests(Model model){
        return findPaginated(1,model);
    }
    // Without sorting
    @GetMapping("/list/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model){
        int pageSize = 4;
        Page<Guest> page = guestService.findPaginated(pageNo,pageSize);
        List<Guest>guests = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("guests", guests);
        return "list-guests";
    }
     */
}
