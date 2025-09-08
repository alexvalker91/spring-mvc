package alex.valker91.spring_boot.config;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.facade.impl.BookingFacadeImpl;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJpaRepositories(basePackages = "alex.valker91.spring_boot.repository")
@EntityScan(basePackages = "alex.valker91.spring_boot.entity")
public class AppConfig {

    @Bean
    public BookingFacade bookingFacade(EventService eventService,
                                       TicketService ticketService,
                                       UserService userService,
                                       UserAccountService userAccountService) {
        return new BookingFacadeImpl(eventService, ticketService, userService, userAccountService);
    }
}
