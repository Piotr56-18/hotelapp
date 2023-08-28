package com.piotr.springboot.hotelapp.domain.reservation;
import com.piotr.springboot.hotelapp.domain.guest.Guest;
import com.piotr.springboot.hotelapp.domain.guest.GuestService;
import com.piotr.springboot.hotelapp.domain.room.Room;
import com.piotr.springboot.hotelapp.domain.room.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
@TestPropertySource("/application.properties.for.testing")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private GuestService guestService;

    @Test
    public void testListReservations() throws Exception {
        Page<Reservation> mockPage = new PageImpl<>(Arrays.asList(new Reservation(), new Reservation()));
        when(reservationService.findPaginated(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/reservations/list/1").param("sortField", "lastName")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(view().name("list-reservations"));
    }

    @Test
    public void testAddReservationForm() throws Exception {
        List<Guest> mockGuestList = Arrays.asList(new Guest(), new Guest());
        List<Room> mockRoomList = Arrays.asList(new Room(), new Room());
        when(guestService.findAll()).thenReturn(mockGuestList);
        when(roomService.findAll()).thenReturn(mockRoomList);

        mockMvc.perform(get("/reservations/addReservationForm"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attributeExists("guestList"))
                .andExpect(model().attributeExists("roomList"))
                .andExpect(view().name("reservation-form"));
    }

    @Test
    public void testUpdateReservationForm() throws Exception {
        Long reservationId = 1L;
        Reservation mockReservation = new Reservation();
        List<Guest> mockGuestList = Arrays.asList(new Guest(), new Guest());
        List<Room> mockRoomList = Arrays.asList(new Room(), new Room());

        when(reservationService.findById(reservationId)).thenReturn(mockReservation);
        when(guestService.findAll()).thenReturn(mockGuestList);
        when(roomService.findAll()).thenReturn(mockRoomList);

        mockMvc.perform(get("/reservations/updateReservationForm").param("reservationId", reservationId.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attributeExists("guestList"))
                .andExpect(model().attributeExists("roomList"))
                .andExpect(view().name("reservation-form"));
    }

    @Test
    public void testSaveReservation() throws Exception {
        mockMvc.perform(post("/reservations/save")
                        .param("guest.id", "1")
                        .param("room.id", "2")
                        .param("startDate", "2023-08-28")
                        .param("endDate", "2023-08-30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/list"));
        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testDeleteReservation() throws Exception {
        Long reservationId = 1L;
        mockMvc.perform(get("/reservations/delete").param("reservationId", reservationId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/list"));
        verify(reservationService, times(1)).deleteById(reservationId);
    }
}