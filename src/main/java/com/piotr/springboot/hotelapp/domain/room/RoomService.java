package com.piotr.springboot.hotelapp.domain.room;

import com.piotr.springboot.hotelapp.domain.reservation.Reservation;
import com.piotr.springboot.hotelapp.domain.reservation.ReservationRepository;
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
public class RoomService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }
    public RoomService(){};
    public Page<Room>findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.roomRepository.findAll(pageable);
    }
    public Page<Room>findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);
        return this.roomRepository.findAll(pageable);
    }

    public List<Room> findAll(){
        return roomRepository.findAll();
    }
    public Room findById(Long id){
        Optional<Room> result = roomRepository.findById(id);
        Room room = null;
        if(result.isPresent()){
            room = result.get();
            return room;
        }else{
            throw new RuntimeException("Don't find room id: " + id);
        }
    }
    public List<Room>getAvailableRooms(LocalDate from, LocalDate to){
        if (from.isAfter(to)){
            throw  new IllegalArgumentException("Start date cant be after end date");
        }
        if((from==null)||(to==null)){
            throw new IllegalArgumentException("You must select start date end end date!");
        }
        List<Room>availableRooms = this.findAll();
        List<Reservation>reservations=reservationRepository.findAll();

        for(Reservation reservation:reservations){
            if(reservation.getFrom().equals(from)){
                availableRooms.remove(reservation.getRoom());
            }else if(reservation.getTo().equals(to)){
                availableRooms.remove(reservation.getRoom());
            }else if(reservation.getFrom().isAfter(from)&&reservation.getFrom().isBefore(to)){
                availableRooms.remove(reservation.getRoom());
            }else if(reservation.getTo().isAfter(from)&&reservation.getFrom().isBefore(to)){
                availableRooms.remove(reservation.getRoom());
            }else if(from.isAfter(reservation.getFrom())&&to.isBefore(reservation.getTo())){
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }
    public void save (Room room){
        roomRepository.save(room);
    }
    public void deleteById(Long id){
        roomRepository.deleteById(id);
    }
    public Room findByNumber(int number){
        return roomRepository.findByNumber(number);
    }
}
