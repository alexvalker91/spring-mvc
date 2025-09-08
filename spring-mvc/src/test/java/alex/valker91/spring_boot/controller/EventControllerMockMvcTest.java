package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.model.impl.EventImpl;
import alex.valker91.spring_boot.facade.BookingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EventController.class)
class EventControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingFacade bookingFacade;

    @Test
    void getById_returns_event() throws Exception {
        when(bookingFacade.getEventById(1)).thenReturn(new EventImpl(1, "Mocked", new Date(), 10));
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mocked"));
    }
}

