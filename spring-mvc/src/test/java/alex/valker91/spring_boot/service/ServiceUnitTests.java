package alex.valker91.spring_boot.service;

import alex.valker91.spring_boot.entity.EventDb;
import alex.valker91.spring_boot.entity.TicketDb;
import alex.valker91.spring_boot.entity.UserAccountDb;
import alex.valker91.spring_boot.entity.UserDb;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.UserAccount;
import alex.valker91.spring_boot.model.impl.EventImpl;
import alex.valker91.spring_boot.model.impl.UserAccountImpl;
import alex.valker91.spring_boot.repository.DbEventRepository;
import alex.valker91.spring_boot.repository.DbTicketRepository;
import alex.valker91.spring_boot.repository.DbUserAccountRepository;
import alex.valker91.spring_boot.repository.DbUserRepository;
import alex.valker91.spring_boot.service.impl.DbEventServiceImpl;
import alex.valker91.spring_boot.service.impl.DbTicketServiceImpl;
import alex.valker91.spring_boot.service.impl.DbUserAccountServiceImpl;
import alex.valker91.spring_boot.service.impl.DbUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ServiceUnitTests {

    @Test
    void eventService_getById_maps_correctly() {
        DbEventRepository repo = Mockito.mock(DbEventRepository.class);
        EventDb edb = new EventDb(1, "E", new Date(), 10);
        when(repo.findById(1L)).thenReturn(Optional.of(edb));

        DbEventServiceImpl svc = new DbEventServiceImpl();
        // inject via reflection since field is package-private autowired
        try { var f = DbEventServiceImpl.class.getDeclaredField("dbEventRepository"); f.setAccessible(true); f.set(svc, repo);} catch (Exception ignored) {}

        Event e = svc.getEventById(1);
        assertThat(e.getTitle()).isEqualTo("E");
    }

    @Test
    void ticketService_bookTicket_saves_and_returns_ticket() {
        DbTicketRepository repo = Mockito.mock(DbTicketRepository.class);
        when(repo.existsByEventIdAndPlace(1L, 1)).thenReturn(false);
        TicketDb saved = new TicketDb(); saved.setId(10L); saved.setEventId(1); saved.setUserId(2); saved.setPlace(1); saved.setCategory(TicketDb.Category.STANDARD);
        when(repo.save(any(TicketDb.class))).thenReturn(saved);

        DbTicketServiceImpl svc = new DbTicketServiceImpl();
        try { var f = DbTicketServiceImpl.class.getDeclaredField("dbTicketRepository"); f.setAccessible(true); f.set(svc, repo);} catch (Exception ignored) {}

        Ticket t = svc.bookTicket(2, 1, 1, Ticket.Category.STANDARD);
        assertThat(t.getId()).isEqualTo(10);
    }

    @Test
    void userService_getUserByEmail_maps_correctly() {
        DbUserRepository repo = Mockito.mock(DbUserRepository.class);
        when(repo.findByEmail("a@b.c")).thenReturn(Optional.of(new UserDb(1L, "A", "a@b.c")));
        DbUserServiceImpl svc = new DbUserServiceImpl();
        try { var f = DbUserServiceImpl.class.getDeclaredField("dbUserRepository"); f.setAccessible(true); f.set(svc, repo);} catch (Exception ignored) {}
        assertThat(svc.getUserByEmail("a@b.c")).isNotNull();
    }

    @Test
    void userAccountService_refill_and_getByUserId_use_custom_repo_method() {
        DbUserAccountRepository repo = Mockito.mock(DbUserAccountRepository.class);
        UserAccountDb uadb = new UserAccountDb(1L, 1L, 10);
        when(repo.findByUserId(1L)).thenReturn(Optional.of(uadb));
        when(repo.save(any(UserAccountDb.class))).thenAnswer(inv -> inv.getArgument(0));
        DbUserAccountServiceImpl svc = new DbUserAccountServiceImpl();
        try { var f = DbUserAccountServiceImpl.class.getDeclaredField("dbUserAccountRepository"); f.setAccessible(true); f.set(svc, repo);} catch (Exception ignored) {}

        int refilled = svc.refillUserAccount(1, 5);
        assertThat(refilled).isEqualTo(5);
        UserAccount ua = svc.getUserAccountByUserId(1);
        assertThat(ua.getUserAmount()).isGreaterThanOrEqualTo(10);
    }
}

