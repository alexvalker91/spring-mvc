package alex.valker91.spring_boot.dao;

import alex.valker91.spring_boot.model.Event;

import java.util.List;

public interface EventDAO {

    Event getById(long id);

    List<Event> getAll();

    Event insert(Event event);

    Event update(Event event);

    boolean delete(long eventId);
}
