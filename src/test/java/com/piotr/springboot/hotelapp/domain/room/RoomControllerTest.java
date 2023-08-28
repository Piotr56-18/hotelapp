package com.piotr.springboot.hotelapp.domain.room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@TestPropertySource("/application.properties.for.testing")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    public void testListRooms() throws Exception {
        Page<Room> mockPage = new PageImpl<>(Arrays.asList(new Room(), new Room()));
        when(roomService.findPaginated(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/rooms/list/1").param("sortField", "lastName")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rooms"))
                .andExpect(view().name("list-rooms"));
    }

    @Test
    public void testAddRoomForm() throws Exception {
        mockMvc.perform(get("/rooms/addRoomForm"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("room-form"));
    }

    @Test
    public void testUpdateRoomForm() throws Exception {
        Long roomId = 1L;
        Room mockRoom = new Room();
        when(roomService.findById(roomId)).thenReturn(mockRoom);

        mockMvc.perform(get("/rooms/updateRoomForm").param("roomId", roomId.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("update-room-form"));
    }

    @Test
    public void testSaveRoomWithValidInput() throws Exception {
        List<BedType>beds = new ArrayList<>();
        beds.add(BedType.SINGLE);
        mockMvc.perform(post("/rooms/save")
                        .param("number", "101")
                        .param("beds","SINGLE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/list"));
        verify(roomService, times(1)).save(any(Room.class));
    }

    @Test
    public void testSaveRoomWithDuplicateNumber() throws Exception {
        when(roomService.findByNumber(anyInt())).thenReturn(new Room());

        mockMvc.perform(post("/rooms/save")
                        .param("number", "101")
                        .param("beds","SINGLE"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("room", "number"))
                .andExpect(view().name("room-form"));
        verify(roomService, times(1)).findByNumber(101);
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Long roomId = 1L;
        mockMvc.perform(get("/rooms/delete").param("roomId", roomId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/list"));
        verify(roomService, times(1)).deleteById(roomId);
    }
}