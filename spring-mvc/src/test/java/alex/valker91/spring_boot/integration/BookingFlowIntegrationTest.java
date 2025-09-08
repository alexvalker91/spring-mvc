package alex.valker91.spring_boot.integration;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.entity.EventDb;
import alex.valker91.spring_boot.entity.UserDb;
import alex.valker91.spring_boot.entity.UserAccountDb;
import alex.valker91.spring_boot.repository.DbEventRepository;
import alex.valker91.spring_boot.repository.DbUserRepository;
import alex.valker91.spring_boot.repository.DbUserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookingFlowIntegrationTest {

    @Autowired
    BookingFacade bookingFacade;
    @Autowired
    DbEventRepository eventRepository;
    @Autowired
    DbUserRepository userRepository;
    @Autowired
    DbUserAccountRepository userAccountRepository;

    @Test
    void book_and_cancel_ticket_flow() {
        // seed minimal data
        UserDb user = userRepository.save(new UserDb(null, "ITest", "itest@example.com"));
        EventDb event = new EventDb();
        event.setTitle("ITest Event");
        event.setDate(new java.util.Date());
        event.setTicketPrice(100);
        event = eventRepository.save(event);
        userAccountRepository.save(new UserAccountDb(null, user.getId(), 200));

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 2, Ticket.Category.STANDARD);
        assertThat(ticket).isNotNull();

        boolean cancelled = bookingFacade.cancelTicket(ticket.getId());
        assertThat(cancelled).isTrue();
    }
}

