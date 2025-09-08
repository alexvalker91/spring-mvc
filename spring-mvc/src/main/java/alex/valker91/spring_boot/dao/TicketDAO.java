package alex.valker91.spring_boot.dao;

import alex.valker91.spring_boot.model.Ticket;

import java.util.List;

public interface TicketDAO {

    Ticket getById(long id);

    List<Ticket> getAll();

    Ticket insert(Ticket ticket);

    Ticket update(Ticket ticket);

    boolean delete(long ticketId);
}
