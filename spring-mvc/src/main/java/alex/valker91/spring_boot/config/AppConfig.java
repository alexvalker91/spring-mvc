package alex.valker91.spring_boot.config;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.facade.impl.BookingFacadeImpl;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BookingFacade bookingFacade(EventService eventService,
                                       TicketService ticketService,
                                       UserService userService,
                                       UserAccountService userAccountService) {
        return new BookingFacadeImpl(eventService, ticketService, userService, userAccountService);
    }
}
