package com.piotr.springboot.hotelapp.domain.guest;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@TestPropertySource("/application.properties.for.testing")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(GuestController.class)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Test
    public void testListGuests() throws Exception {
        Page<Guest> mockPage = new PageImpl<>(Arrays.asList(new Guest(), new Guest()));
        when(guestService.findPaginated(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockPage);
        mockMvc.perform(get("/guests/list/1").param("sortField", "lastName")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("guests"))
                .andExpect(view().name("list-guests"));
    }

    @Test
    public void testAddGuestForm() throws Exception {
        mockMvc.perform(get("/guests/addGuestForm"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("guest"))
                .andExpect(view().name("guest-form"));
    }

    @Test
    public void testUpdateGuestForm() throws Exception {
        Long guestId = 1L;
        Guest mockGuest = new Guest();
        when(guestService.findById(guestId)).thenReturn(mockGuest);

        mockMvc.perform(get("/guests/updateGuestForm").param("guestId", guestId.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("guest"))
                .andExpect(view().name("guest-form"));
    }

    @Test
    public void testSaveGuestWithValidInput() throws Exception {
        mockMvc.perform(post("/guests/save")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@gmail.com")
                        .param("age", "18"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/guests/list"));
    }

    @Test
    public void testSaveGuestWithInvalidInput() throws Exception {
        mockMvc.perform(post("/guests/save"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("guest"))
                .andExpect(view().name("guest-form"));
    }

    @Test
    public void testDeleteGuest() throws Exception {
        Long guestId = 1L;

        mockMvc.perform(get("/guests/delete").param("guestId", guestId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/guests/list"));

        verify(guestService, times(1)).deleteById(guestId);
    }
}