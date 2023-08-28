package com.piotr.springboot.hotelapp.domain.guest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
//Tests with H2 DB in mem
@TestPropertySource("/application.properties.for.testing")
@SpringBootTest
class GuestServiceTest {
    @Autowired
    GuestService guestService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeEach
    public void setup(){
        jdbcTemplate.execute("insert into guest(id, first_name, last_name, age, gender) values (1, 'Piotr', 'Nowak', 20, 'MALE')");
    }
    @Test
    public void createGuestAndFindAllTest(){
        Guest guest = new Guest( "Kamil", "Kowal", 33, Gender.MALE);
        guest.setId(2L);
        guestService.save(guest);
        assertEquals(2, guestService.findAll().size());
    }
    @Test
    public void createGuestWithWrongDataTest(){
        Guest guest = new Guest( " ", "Kowal", 33, Gender.MALE);
        guest.setId(2L);
        assertThrows(ConstraintViolationException.class,()->{
            guestService.save(guest);
        });
        Guest guest2 = new Guest( "Piotr", " ", 33, Gender.MALE);
        guest2.setId(3L);
        assertThrows(ConstraintViolationException.class,()->{
            guestService.save(guest2);
        });
        Guest guest3 = new Guest( "Piotr", "Kowal", -1, Gender.MALE);
        guest3.setId(4L);
        assertThrows(ConstraintViolationException.class,()->{
            guestService.save(guest3);
        });
        Guest guest4 = new Guest( "Piotr", "Kowal", 122, Gender.MALE);
        guest4.setId(5L);
        assertThrows(ConstraintViolationException.class,()->{
            guestService.save(guest4);
        });

    }
    @Test
    public void findByIdThrowsExceptionTest(){
        assertThrows(RuntimeException.class,()->{
            guestService.findById(10L);
        });
    }
    @Test
    public void findByIdTest(){
        Guest guest = guestService.findById(1L);
        assertEquals(1,guest.getId());
        assertEquals("Piotr",guest.getFirstName());
        assertEquals("Nowak",guest.getLastName());
        assertEquals(20,guest.getAge());
        assertEquals(Gender.MALE,guest.getGender());
    }
    @Test
    public void deleteGuestTest(){
        assertNotNull(guestService.findById(1L));
        guestService.deleteById(1L);
        assertThrows(RuntimeException.class,()->{
            guestService.findById(1L);
        });
    }
    @Test
    public void deleteGuestWithWrongIdTest(){
        assertThrows(RuntimeException.class,()->{
            guestService.findById(99L);
        });
        assertThrows(EmptyResultDataAccessException.class,()-> {
            guestService.deleteById(99L);
        });
    }
    @AfterEach
    public void clearDB(){
        jdbcTemplate.execute("delete from guest");
    }
}