package alex.valker91.spring_boot.config;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.facade.impl.BookingFacadeImpl;
import alex.valker91.spring_boot.oxm.TicketXml;
import alex.valker91.spring_boot.oxm.TicketsXml;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class AppConfig {

    @Bean
    public BookingFacade bookingFacade(EventService eventService,
                                       TicketService ticketService,
                                       UserService userService,
                                       UserAccountService userAccountService,
                                       Jaxb2Marshaller ticketsMarshaller,
                                       TransactionTemplate transactionTemplate) {
        return new BookingFacadeImpl(eventService, ticketService, userService, userAccountService,
                ticketsMarshaller, transactionTemplate);
    }

    @Bean
    public Jaxb2Marshaller ticketsMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(TicketsXml.class, TicketXml.class);
        return marshaller;
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
