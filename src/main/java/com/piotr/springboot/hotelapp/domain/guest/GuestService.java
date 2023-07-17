package com.piotr.springboot.hotelapp.domain.guest;

import com.piotr.springboot.hotelapp.domain.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private GuestRepository guestRepository;

    @Autowired
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Page<Guest> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.guestRepository.findAll(pageable);
    }
    public Page<Guest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortField).ascending():Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);
        return this.guestRepository.findAll(pageable);
    }
    public List<Guest>findAll(){
        return guestRepository.findAll();
    };
    public Guest findById(Long id){
        Optional<Guest>result = guestRepository.findById(id);
        Guest guest = null;
        if(result.isPresent()){
            guest = result.get();
        }else{
            throw  new RuntimeException("Don't find guest id: " + id);
        }
        return guest;
    }
    public void save (Guest guest){
        guestRepository.save(guest);
    }
    public void deleteById(Long id){
        guestRepository.deleteById(id);
    }
}
