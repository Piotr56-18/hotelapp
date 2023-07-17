package com.piotr.springboot.hotelapp.domain.reservation;

import com.piotr.springboot.hotelapp.domain.guest.GuestService;
import com.piotr.springboot.hotelapp.domain.room.Room;
import com.piotr.springboot.hotelapp.domain.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private GuestService guestService;
    private RoomService roomService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, GuestService guestService, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.guestService = guestService;
        this.roomService = roomService;
    }
    public Page<Reservation> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.reservationRepository.findAll(pageable);
    }
    public Page<Reservation> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);
        return this.reservationRepository.findAll(pageable);
    }

    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    };
    public Reservation findById(Long id){
        Optional<Reservation> result = reservationRepository.findById(id);
        Reservation reservation = null;
        if(result.isPresent()){
            reservation = result.get();
        }else{
            throw  new RuntimeException("Don't find reservation id: " + id);
        }
        return reservation;
    }

    public void save (Reservation reservation){
        reservationRepository.save(reservation);
    }
    public void deleteById(Long id){
        reservationRepository.deleteById(id);
    }
}

