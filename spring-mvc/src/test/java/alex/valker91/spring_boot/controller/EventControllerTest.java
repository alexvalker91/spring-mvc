package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.impl.EventImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingFacade bookingFacade;

    @Test
    void getEventsMainPageReturnsTemplate() throws Exception {
        mockMvc.perform(get("/events/main/page"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/main_page"));
    }

    @Test
    void createEventSuccessRedirectsToEvent() throws Exception {
        Event created = new EventImpl(1L, "Conf", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-01-01 10:00:00"), 100);
        when(bookingFacade.createEvent(any(Event.class))).thenReturn(created);

        mockMvc.perform(post("/events/create")
                        .param("id", "0")
                        .param("title", "Conf")
                        .param("date", "2025-01-01 10:00:00")
                        .param("ticketPrice", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/1"));
    }

    @Test
    void createEventParseErrorReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/events/create")
                        .param("id", "0")
                        .param("title", "Conf")
                        .param("date", "bad-date")
                        .param("ticketPrice", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("events/new_event"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    void getEventsForDayBadDateSetsBadRequestAndReturnsListView() throws Exception {
        mockMvc.perform(get("/events/list/by-day")
                        .param("day", "2025/01/01")
                        .param("pageSize", "10")
                        .param("pageNum", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("events/list_events"));
    }
}
