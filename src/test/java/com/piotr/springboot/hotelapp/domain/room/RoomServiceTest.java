package com.piotr.springboot.hotelapp.domain.room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDate;
//Tests with H2 DB in mem
import static org.junit.jupiter.api.Assertions.*;
@TestPropertySource("/application.properties.for.testing")
@SpringBootTest
public class RoomServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeEach
    public void setup(){
        jdbcTemplate.execute("insert into room (id, number) values (1, 100)");
        jdbcTemplate.execute("insert into room_beds (room_id, beds) values (1, 'SINGLE')");
    }
    @Test
    public void assertThrowsIllegalArgumentExceptionWhenFromDateIsAfterToDate(){
        //set up
        LocalDate from = LocalDate.of(2023, 7, 26);
        LocalDate to = LocalDate.of(2023, 7, 8);
        //assert
        assertThrows(Exception.class, ()->{
            roomService.getAvailableRooms(from, to);
        });
    }
    @Test
    public void assertThrowsIllegalArgumentExceptionWhenFromDateIsNull(){
        //set up
        LocalDate from = null;
        LocalDate to = LocalDate.of(2023, 7, 8);
        //assert
        assertThrows(Exception.class, ()->{
            roomService.getAvailableRooms(from, to);
        });
    }
    @Test
    public void assertThrowsIllegalArgumentExceptionWhenToDateIsNull(){
        //set up
        LocalDate to = null;
        LocalDate from = LocalDate.of(2023, 7, 8);
        //assert
        assertThrows(NullPointerException.class, ()->{
            roomService.getAvailableRooms(from, to);
        });
    }
    @Test
    public void findByIdTestAndRoomExist(){
        assertNotNull(roomService.findById(1L));
        assertEquals(100, roomService.findById(1L).getNumber(), "number");
        assertEquals("[Single]", roomService.findById(1L).getBeds().toString(), "beds");
    }
    @Test
    public void findByIdTestAndRoomDoesNotExist(){
        assertThrows(RuntimeException.class, ()->{
            roomService.findById(99L);
        });
    }
    @Test
    public void deleteByIdTestAndRoomExist(){
        assertNotNull(roomService.findById(1L));
        roomService.deleteById(1L);
        assertThrows(RuntimeException.class, ()->{
            roomService.findById(1L);
        });
    }
    @Test
    public void deleteByIdTestAndRoomDoesNotExist(){
        assertThrows(RuntimeException.class, ()->{
            roomService.deleteById(99L);
        });
    }
    @Test
    void findAllTestWithListOfRooms(){
        jdbcTemplate.execute("insert into room (id, number) values (2, 101)");
        jdbcTemplate.execute("insert into room_beds (room_id, beds) values (2, 'SINGLE')");
        jdbcTemplate.execute("insert into room (id, number) values (3, 102)");
        jdbcTemplate.execute("insert into room_beds (room_id, beds) values (3, 'SINGLE')");
        assertEquals(3,roomService.findAll().size());
    }
    @Test
    void findAllTestWithNoRooms(){
        jdbcTemplate.execute("delete from room_beds");
        jdbcTemplate.execute("delete from room");
        assertEquals(0,roomService.findAll().size());
    }
    @Test
    public void findByNumberTestAndRoomExist(){
        assertEquals(100, roomService.findByNumber(100).getNumber(), "number");
        assertEquals(1, roomService.findByNumber(100).getId(), "id");
    }
    @Test
    public void findByNumberTestAndRoomDoesNotExist(){
        assertNull(roomService.findByNumber(200));
    }
    @Test
    public void findByNumberTestWithBedsAndRoomExist(){
        assertEquals(100, roomService.findByNumber(100).getNumber(), "number");
        assertEquals(1, roomService.findByNumber(100).getId(), "id");
        assertEquals("[Single]", roomService.findByNumber(100).getBeds().toString(), "beds");
    }
    @AfterEach
    public void clearDB(){
        jdbcTemplate.execute("delete from room_beds");
        jdbcTemplate.execute("delete from room");

    }
}