package com.piotr.springboot.hotelapp.domain.reservation;

import com.piotr.springboot.hotelapp.domain.guest.Guest;
import com.piotr.springboot.hotelapp.domain.guest.GuestDTO;
import com.piotr.springboot.hotelapp.domain.guest.GuestService;
import com.piotr.springboot.hotelapp.domain.room.Room;
import com.piotr.springboot.hotelapp.domain.room.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;


@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;
    private RoomService roomService;
    private GuestService guestService;

    @Autowired
    public ReservationController(ReservationService reservationService, RoomService roomService, GuestService guestService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.guestService = guestService;
    }
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    }
    /*
    @GetMapping("/list")
    public String listReservations(Model model){
        List<Reservation> reservations = reservationService.findAll();
        model.addAttribute("reservations", reservations);
        return "list-reservations";
    }

    @GetMapping("/list")
    public String listReservations(Model model){
        return findPaginated(1,model);
    }

    @GetMapping("/list/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model){
        int pageSize = 3;
        Page<Reservation> page = reservationService.findPaginated(pageNo,pageSize);
        List<Reservation>reservations = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("reservations", reservations);
        return "list-reservations";
    }
     */
    @GetMapping("/list/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model){
        int pageSize = 3;
        Page<Reservation> page = reservationService.findPaginated(pageNo,pageSize, sortField,sortDir);
        List<Reservation>reservations = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");

        model.addAttribute("reservations", reservations);
        return "list-reservations";
    }
    @GetMapping("/list")
    public String listReservations(Model model){
        return findPaginated(1,"from", "asc", model);
    }
    @GetMapping("/createReservationDate")
    public String createReservationDate(){
        System.out.println("Choose start and end of reservation!");
        return "createReservationDate";
    }
    @PostMapping(value = "/createReservationRoomChoosing")
    public String createReservationRoomChoosing(ReservationDTO reservationDTO, Model model){
        try {
            List<Room> rooms = roomService.getAvailableRooms(reservationDTO.getFrom(), reservationDTO.getTo());
            model.addAttribute("rooms", rooms);
            model.addAttribute("reservationDTO", reservationDTO);
            return "createReservationRoomChoosing";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", "Error occurred!" + e.getMessage());
            model.addAttribute("errorOccurred", true);
            return "createReservationDate";
        }
    }

    @PostMapping(value = "/createReservationGuestDetails")
    public String createReservationGuestDetails(@RequestParam("from") LocalDate from,
                                                @RequestParam("to") LocalDate to,
                                                @RequestParam("roomId") Long roomId,
                                                Model model){
        ReservationDTO reservationDTO = new ReservationDTO(from, to, roomId);
        model.addAttribute("reservationDTO", reservationDTO);
        System.out.println("=================================");
        System.out.println(from);
        System.out.println(to);
        System.out.println(roomId);
        return "createReservationGuestDetails";
    }
    @PostMapping(value = "/createReservationSuccess")
    public String createReservationSuccess(@ModelAttribute("reservationDTO") ReservationDTO reservationDTO,
                                           Model model){
        Guest guest = new Guest(reservationDTO.getGuestDTO().getFirstName(),
                reservationDTO.getGuestDTO().getLastName(), reservationDTO.getGuestDTO().getAge(),
                reservationDTO.getGuestDTO().getGender());
        guestService.save(guest);
        reservationService.save(new Reservation(guest,roomService.findById(reservationDTO.getRoomId()), reservationDTO.getFrom(), reservationDTO.getTo()));
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("roomNumber", roomService.findById(reservationDTO.getRoomId()).getNumber());
        System.out.println("=================================");
        System.out.println(reservationDTO.getGuestDTO().getAge());
        System.out.println(reservationDTO.getGuestDTO().getFirstName());
        System.out.println(reservationDTO.getGuestDTO().getLastName());
        System.out.println(reservationDTO.getGuestDTO().getGender());
        return "createReservationSuccess";
    }

    @GetMapping("/addReservationForm")
    public String addReservationForm(Model model){
        Reservation reservation = new Reservation();
        List<Guest>guestList = guestService.findAll();
        List<Room>roomList = roomService.findAll();
        model.addAttribute("reservation", reservation);
        model.addAttribute("roomList", roomList);
        model.addAttribute("guestList", guestList);
        return "reservation-form";
    }
    @GetMapping("/updateReservationForm")
    public String updateReservationForm(@RequestParam("reservationId") Long id, Model model){
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("guestList", guestService.findAll());
        return "reservation-form";
    }
    @PostMapping("/save")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation){
        reservationService.save(reservation);
        return "redirect:/reservations/list";
    }
    @GetMapping("/delete")
    public String deleteReservation(@RequestParam("reservationId") Long id){
        reservationService.deleteById(id);
        return "redirect:/reservations/list";
    }

}
