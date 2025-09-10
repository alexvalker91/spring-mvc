package alex.valker91.spring_boot.facade;

import alex.valker91.spring_boot.facade.impl.BookingFacadeImpl;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.UserAccount;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingFacadeImplTest {

	private EventService eventService;
	private TicketService ticketService;
	private UserService userService;
	private UserAccountService userAccountService;
	private Jaxb2Marshaller marshaller;
	private TransactionTemplate transactionTemplate;

	private BookingFacadeImpl bookingFacade;

	@BeforeEach
	void setUp() {
		eventService = mock(EventService.class);
		ticketService = mock(TicketService.class);
		userService = mock(UserService.class);
		userAccountService = mock(UserAccountService.class);
		marshaller = mock(Jaxb2Marshaller.class);
		transactionTemplate = mock(TransactionTemplate.class);
		bookingFacade = new BookingFacadeImpl(eventService, ticketService, userService, userAccountService, marshaller, transactionTemplate);
	}

	@Test
	void bookTicket_returnsNull_whenInsufficientFunds() {
		Event event = mock(Event.class);
		when(eventService.getEventById(10L)).thenReturn(event);
		when(event.getTicketPrice()).thenReturn(100);

		UserAccount account = mock(UserAccount.class);
		when(userAccountService.getUserAccountByUserId(5L)).thenReturn(account);
		when(account.getUserAmount()).thenReturn(50);

		Ticket result = bookingFacade.bookTicket(5L, 10L, 7, Ticket.Category.PREMIUM);

		assertThat(result).isNull();
		verify(ticketService, never()).bookTicket(anyLong(), anyLong(), anyInt(), any());
		verify(userAccountService, never()).updateUserAccount(any());
	}

	@Test
	void bookTicket_booksAndDeducts_whenEnoughFunds() {
		Event event = mock(Event.class);
		when(eventService.getEventById(10L)).thenReturn(event);
		when(event.getTicketPrice()).thenReturn(100);

		UserAccount account = mock(UserAccount.class);
		when(userAccountService.getUserAccountByUserId(5L)).thenReturn(account);
		when(account.getUserAmount()).thenReturn(150);

		Ticket booked = mock(Ticket.class);
		when(ticketService.bookTicket(5L, 10L, 7, Ticket.Category.PREMIUM)).thenReturn(booked);

		Ticket result = bookingFacade.bookTicket(5L, 10L, 7, Ticket.Category.PREMIUM);

		assertThat(result).isSameAs(booked);
		verify(account).setUserAmount(50);
		verify(userAccountService).updateUserAccount(account);
		verify(ticketService).bookTicket(5L, 10L, 7, Ticket.Category.PREMIUM);
	}

	@Test
	void cancelTicket_refundsAndCancels() {
		Ticket ticket = mock(Ticket.class);
		when(ticket.getEventId()).thenReturn(10L);
		when(ticket.getUserId()).thenReturn(5L);
		when(ticketService.getById(77L)).thenReturn(ticket);

		Event event = mock(Event.class);
		when(eventService.getEventById(10L)).thenReturn(event);
		when(event.getTicketPrice()).thenReturn(100);

		when(ticketService.cancelTicket(77L)).thenReturn(true);

		boolean result = bookingFacade.cancelTicket(77L);

		assertThat(result).isTrue();
		verify(userAccountService).refillUserAccount(5L, 100);
		verify(ticketService).cancelTicket(77L);
	}
}