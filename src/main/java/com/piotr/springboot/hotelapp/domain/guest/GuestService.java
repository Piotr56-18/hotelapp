package com.piotr.springboot.hotelapp.domain.guest;

import org.springframework.beans.factory.annotation.Autowired;
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
