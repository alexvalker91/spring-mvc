package alex.valker91.spring_boot.service;

import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.User;

import java.util.List;

public interface TicketService {

    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);

    Ticket getById(long id);
}