package alex.valker91.spring_boot.repository;

import alex.valker91.spring_boot.entity.EventDb;
import alex.valker91.spring_boot.entity.TicketDb;
import alex.valker91.spring_boot.entity.UserAccountDb;
import alex.valker91.spring_boot.entity.UserDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DbRepositoriesTest {

    @Autowired
    DbEventRepository eventRepository;
    @Autowired
    DbUserRepository userRepository;
    @Autowired
    DbUserAccountRepository userAccountRepository;
    @Autowired
    DbTicketRepository ticketRepository;

    @Test
    void eventRepository_crud_and_queries_work() {
        EventDb e = new EventDb();
        e.setTitle("Repo Test");
        e.setDate(new Date());
        e.setTicketPrice(123);
        EventDb saved = eventRepository.save(e);
        assertThat(saved.getId()).isNotNull();

        assertThat(eventRepository.findById(saved.getId())).isPresent();
        assertThat(eventRepository.findByTitleContainingIgnoreCase("repo", org.springframework.data.domain.PageRequest.of(0, 10)).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void userRepository_crud_and_custom_queries_work() {
        UserDb u = new UserDb(null, "Charlie", "charlie@example.com");
        UserDb saved = userRepository.save(u);
        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findByEmail("charlie@example.com")).isPresent();
        assertThat(userRepository.findAllByName("Charlie", org.springframework.data.domain.PageRequest.of(0, 10)).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void userAccountRepository_findByUserId_works() {
        UserDb u = userRepository.save(new UserDb(null, "Dana", "dana@example.com"));
        UserAccountDb acc = new UserAccountDb(null, u.getId(), 50);
        UserAccountDb saved = userAccountRepository.save(acc);
        assertThat(saved.getId()).isNotNull();
        assertThat(userAccountRepository.findByUserId(u.getId())).isPresent();
    }

    @Test
    void ticketRepository_queries_work() {
        UserDb u = userRepository.save(new UserDb(null, "Eve", "eve@example.com"));
        EventDb e = new EventDb();
        e.setTitle("T1");
        e.setDate(new Date());
        e.setTicketPrice(10);
        e = eventRepository.save(e);
        TicketDb t = new TicketDb();
        t.setEventId(e.getId());
        t.setUserId(u.getId());
        t.setCategory(TicketDb.Category.STANDARD);
        t.setPlace(5);
        TicketDb saved = ticketRepository.save(t);
        assertThat(saved.getId()).isNotNull();
        assertThat(ticketRepository.findAllByUserId(u.getId(), org.springframework.data.domain.PageRequest.of(0, 10)).getContent()).isNotEmpty();
        assertThat(ticketRepository.findAllByEventId(e.getId(), org.springframework.data.domain.PageRequest.of(0, 10)).getContent()).isNotEmpty();
        assertThat(ticketRepository.existsByEventIdAndPlace(e.getId(), 5)).isTrue();
    }
}

