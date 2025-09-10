package alex.valker91.spring_boot.facade.impl;

import org.springframework.transaction.annotation.Transactional;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.User;
import alex.valker91.spring_boot.model.UserAccount;
import alex.valker91.spring_boot.oxm.TicketXml;
import alex.valker91.spring_boot.oxm.TicketsXml;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.xml.transform.stream.StreamSource;

public class BookingFacadeImpl implements BookingFacade {

    private final EventService eventService;

    private final TicketService ticketService;

    private final UserService userService;

    private final UserAccountService userAccountService;

    public BookingFacadeImpl(EventService eventService,
                             TicketService ticketService,
                             UserService userService,
                             UserAccountService userAccountService,
                             Jaxb2Marshaller ticketsMarshaller,
                             TransactionTemplate transactionTemplate,
                             Resource defaultTicketsResource) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
        this.ticketsMarshaller = ticketsMarshaller;
        this.transactionTemplate = transactionTemplate;
        this.defaultTicketsResource = defaultTicketsResource;
    }

    @Override
    public Event getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    @Transactional
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        Event event = eventService.getEventById(eventId);
        int ticketPrice = event.getTicketPrice();

        UserAccount userAccount = userAccountService.getUserAccountByUserId(userId);
        int userAmount = userAccount.getUserAmount();

        if (ticketPrice > userAmount) {
            return null;
        } else {
            userAccount.setUserAmount(userAccount.getUserAmount() - ticketPrice);
            userAccountService.updateUserAccount(userAccount);
            return ticketService.bookTicket(userId, eventId, place, category);
        }
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    @Transactional
    public boolean cancelTicket(long ticketId) {
        Ticket ticket = ticketService.getById(ticketId);
        long eventId = ticket.getEventId();
        Event event = eventService.getEventById(eventId);
        int ticketPrice = event.getTicketPrice();
        long userId = ticket.getUserId();
        userAccountService.refillUserAccount(userId, ticketPrice);
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public int refillUserAccount(long userId, int amount) {
        return userAccountService.refillUserAccount(userId, amount);
    }

    private final Jaxb2Marshaller ticketsMarshaller;
    private final TransactionTemplate transactionTemplate;
    private final Resource defaultTicketsResource;

    @Override
    public void preloadTickets() {
        preloadTickets(null);
    }

    // optional overload to accept custom resource
    private void preloadTickets(Resource overrideResource) {
        final Resource resourceToUse = Objects.requireNonNullElse(overrideResource, defaultTicketsResource);

        TicketsXml tickets;
        try {
            tickets = (TicketsXml) ticketsMarshaller.unmarshal(new StreamSource(resourceToUse.getInputStream()));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read tickets XML resource", e);
        }

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (TicketXml t : tickets.getTickets()) {
                    Ticket.Category category = Ticket.Category.valueOf(t.getCategory().toUpperCase());
                    Ticket booked = ticketService.bookTicket(t.getUser(), t.getEvent(), t.getPlace(), category);
                    if (booked == null) {
                        throw new IllegalStateException("Failed to book ticket for user=" + t.getUser() + ", event=" + t.getEvent() + ", place=" + t.getPlace());
                    }
                }
            }
        });
    }
}
