package com.piotr.springboot.hotelapp.domain.reservation;
import com.piotr.springboot.hotelapp.domain.guest.GuestService;
import com.piotr.springboot.hotelapp.domain.room.RoomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDate;
//Tests with H2 DB in mem
import static org.junit.jupiter.api.Assertions.*;
@TestPropertySource("/application.properties.for.testing")
@SpringBootTest
class ReservationServiceTest {
    @Autowired
    GuestService guestService;
    @Autowired
    RoomService roomService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeEach
    public void setup(){
        jdbcTemplate.execute("insert into guest(id, first_name, last_name, age, gender) values (1, 'Piotr', 'Nowak', 20, 'MALE')");
        jdbcTemplate.execute("insert into room (id, number) values (1, 100)");
        jdbcTemplate.execute("insert into room_beds (room_id, beds) values (1, 'SINGLE')");
        LocalDate from = LocalDate.of(2023, 7, 8);
        LocalDate to = LocalDate.of(2023, 7, 9);
        Reservation reservation = new Reservation(guestService.findById(1L),roomService.findById(1L),from,to);
        reservationService.save(reservation);
    }



    @Test
    public void findByGuestIdTestAndGuestIdExist(){
        assertNotNull(reservationService.findReservationByGuestId(1L));
        assertEquals(LocalDate.of(2023, 7, 8), reservationService.findReservationByGuestId(1L).getFrom(), "from");
        assertEquals(LocalDate.of(2023, 7, 9), reservationService.findReservationByGuestId(1L).getTo(), "to");
        assertEquals(100, reservationService.findReservationByGuestId(1L).getRoom().getNumber(), "room number");
        assertEquals("Piotr", reservationService.findReservationByGuestId(1L).getGuest().getFirstName(), "guest first name");
    }
    @Test
    public void findByGuestIdTestAndGuestIdDoesNotExist(){
        assertThrows(RuntimeException.class, ()->{
            reservationService.findReservationByGuestId(99L);
        });
    }
    @Test
    public void createReservationTest(){
        LocalDate from = LocalDate.of(2023, 7, 18);
        LocalDate to = LocalDate.of(2023, 7, 19);
        jdbcTemplate.execute("insert into guest(id, first_name, last_name, age, gender) values (2, 'Pawel', 'Kowal', 20, 'MALE')");
        jdbcTemplate.execute("insert into room (id, number) values (2, 200)");
        jdbcTemplate.execute("insert into room_beds (room_id, beds) values (2, 'SINGLE')");
        Reservation reservation = new Reservation(guestService.findById(2L),roomService.findById(2L),from, to);
        reservationService.save(reservation);
        assertEquals(2, reservationService.findAll().size());
        assertEquals(LocalDate.of(2023, 7, 18), reservationService.findReservationByGuestId(2L).getFrom(), "from");
        assertEquals(LocalDate.of(2023, 7, 19), reservationService.findReservationByGuestId(2L).getTo(), "to");
        assertEquals(200, reservationService.findReservationByGuestId(2L).getRoom().getNumber(), "room number");
        assertEquals("Pawel", reservationService.findReservationByGuestId(2L).getGuest().getFirstName(), "guest first name");
    }
    @Test
    public void findByIdTestAndReservationExist(){
        Long id = reservationService.findReservationByGuestId(1L).getId();
        assertNotNull(reservationService.findById(id));
        assertEquals(LocalDate.of(2023, 7, 8), reservationService.findById(id).getFrom(), "from");
        assertEquals(LocalDate.of(2023, 7, 9), reservationService.findById(id).getTo(), "to");
        assertEquals(100, reservationService.findById(id).getRoom().getNumber(), "room number");
        assertEquals("Piotr", reservationService.findById(id).getGuest().getFirstName(), "guest first name");
    }
    @Test
    public void findByIdTestAndReservationDoesNotExist(){
        assertThrows(RuntimeException.class, ()->{
            reservationService.findById(99L);
        });
    }
    @Test
    void findAllTestWithListOfReservations(){
        LocalDate from = LocalDate.of(2023, 7, 11);
        LocalDate to = LocalDate.of(2023, 7, 12);
        Reservation reservation = new Reservation(guestService.findById(1L),roomService.findById(1L),from,to);
        reservationService.save(reservation);
        assertEquals(2,reservationService.findAll().size());
    }
    @Test
    void findAllTestWithNoReservations(){
        jdbcTemplate.execute("delete from reservation");
        jdbcTemplate.execute("delete from guest");
        jdbcTemplate.execute("delete from room_beds");
        jdbcTemplate.execute("delete from room");
        assertEquals(0,reservationService.findAll().size());
    }
    @Test
    public void deleteReservationTest(){
        Long id = reservationService.findReservationByGuestId(1L).getId();
        assertNotNull(reservationService.findById(id));
        reservationService.deleteById(id);
        assertThrows(RuntimeException.class,()->{
            reservationService.findById(id);
        });
    }
    @Test
    public void deleteReservationWithWrongIdTest(){
        assertThrows(RuntimeException.class,()->{
            reservationService.findById(99L);
        });
        assertThrows(EmptyResultDataAccessException.class,()-> {
            reservationService.deleteById(99L);
        });
    }
    @AfterEach
    public void clearDB(){
        jdbcTemplate.execute("delete from reservation");
        jdbcTemplate.execute("delete from guest");
        jdbcTemplate.execute("delete from room_beds");
        jdbcTemplate.execute("delete from room");
    }
}