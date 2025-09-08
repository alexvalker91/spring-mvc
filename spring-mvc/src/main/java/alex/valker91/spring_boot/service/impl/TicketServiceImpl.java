package alex.valker91.spring_boot.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import alex.valker91.spring_boot.dao.impl.TicketDAOImpl;
import alex.valker91.spring_boot.exception.DbException;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.User;
import alex.valker91.spring_boot.model.impl.TicketImpl;
import alex.valker91.spring_boot.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);

    private TicketDAOImpl ticketDAO;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOGGER.log(Level.DEBUG,
                "Start booking a ticket for user with id {}, event with id event {}, place {}, category {}",
                userId, eventId, place, category);

        try {
            Ticket ticket = ticketDAO.insert(createNewTicket(userId, eventId, place, category));

            LOGGER.log(Level.DEBUG, "Successfully booking of the ticket: {}", ticket);

            return ticket;
        } catch (DbException e) {
            LOGGER.log(Level.WARN,
                    "Can not to book a ticket for user with id {}, event with id {}, place {}, category {}",
                    userId, eventId, place, category, e);
            return null;
        }
    }

    private Ticket createNewTicket(long userId, long eventId, int place, Ticket.Category category) {
        return new TicketImpl(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG,
                "Finding all booked tickets by user {} with page size {} and number of page {}",
                user, pageSize, pageNum);

        try {
            List<Ticket> ticketsByUser = ticketDAO.getAllByUser(user, pageSize, pageNum);

            LOGGER.log(Level.DEBUG,
                    "All booked tickets successfully found by user {} with page size {} and number of page {}",
                    user, pageSize, pageNum);

            return ticketsByUser;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to find a list of booked tickets by user '{}'", user, e);
            return null;
        }
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG,
                "Finding all booked tickets by event {} with page size {} and number of page {}",
                event, pageSize, pageNum);

        try {
            List<Ticket> ticketsByUser = ticketDAO.getAllByEvent(event, pageSize, pageNum);

            LOGGER.log(Level.DEBUG,
                    "All booked tickets successfully found by event {} with page size {} and number of page {}",
                    event, pageSize, pageNum);

            return ticketsByUser;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to find a list of booked tickets by event '{}'", event, e);
            return null;
        }
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        LOGGER.log(Level.DEBUG, "Start canceling a ticket with id: {}", ticketId);

        try {
            boolean isRemoved = ticketDAO.delete(ticketId);

            LOGGER.log(Level.DEBUG, "Successfully canceling of the ticket with id: {}", ticketId);

            return isRemoved;
        } catch (DbException e) {
            LOGGER.log(Level.WARN, "Can not to cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }

    @Override
    public Ticket getById(long id) {
        return ticketDAO.getById(id);
    }

    public void setTicketDAO(TicketDAOImpl ticketDAO) {
        this.ticketDAO = ticketDAO;
    }
}
