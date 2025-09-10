package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.impl.TicketImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookingFacade bookingFacade;

	@Test
	void getTicketsMainPage_returnsTemplate() throws Exception {
		mockMvc.perform(get("/tickets/main/page"))
				.andExpect(status().isOk())
				.andExpect(view().name("tickets/main_page"));
	}

	@Test
	void createTicket_success_rendersNewTicketWithModel() throws Exception {
		TicketImpl ticket = new TicketImpl(1L, 2L, 1, Ticket.Category.PREMIUM);
		ticket.setId(123L);
		when(bookingFacade.bookTicket(eq(1L), eq(2L), eq(1), eq(Ticket.Category.PREMIUM))).thenReturn(ticket);

		mockMvc.perform(post("/tickets/create")
					.param("userId", "1")
					.param("eventId", "2")
					.param("place", "1")
					.param("category", "PREMIUM"))
				.andExpect(status().isOk())
				.andExpect(view().name("tickets/new_ticket"))
				.andExpect(model().attributeExists("ticket"));
	}

	@Test
	void createTicket_insufficientFunds_returnsFormWithError() throws Exception {
		when(bookingFacade.bookTicket(anyLong(), anyLong(), anyInt(), any())).thenReturn(null);

		mockMvc.perform(post("/tickets/create")
					.param("userId", "1")
					.param("eventId", "2")
					.param("place", "1")
					.param("category", "PREMIUM"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("tickets/new_ticket"))
				.andExpect(model().attributeExists("errors"));
	}

	@Test
	void cancelTicket_setsModelAndReturnsView() throws Exception {
		when(bookingFacade.cancelTicket(10L)).thenReturn(true);

		mockMvc.perform(post("/tickets/cancel").param("ticketId", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("tickets/cancel_ticket"))
				.andExpect(model().attributeExists("result"))
				.andExpect(model().attributeExists("ticketId"));
	}
}