package alex.valker91.spring_boot.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import alex.valker91.spring_boot.entity.EventDb;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.impl.EventImpl;
import alex.valker91.spring_boot.repository.DbEventRepository;
import alex.valker91.spring_boot.service.EventService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
public class DbEventServiceImpl implements EventService {

    private static final Logger LOGGER = LogManager.getLogger(DbEventServiceImpl.class);

    @Autowired
    private DbEventRepository dbEventRepository;

    @Override
    public Event getEventById(long eventId) {
        LOGGER.log(Level.DEBUG, "Finding an event by id: {}", eventId);
        Optional<EventDb> eventDbOptional = this.dbEventRepository.findById(eventId);
        if (eventDbOptional.isPresent()) {
            Event event = mapEventDbToEvent(eventDbOptional.get());
            LOGGER.log(Level.DEBUG, "Event with id {} successfully found ", eventId);
            return event;
        } else {
            LOGGER.log(Level.WARN, "Can not to find an event by id: " + eventId);
            return null;
        }
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<EventDb> events = this.dbEventRepository.findByTitleContainingIgnoreCase(title, pageable);
        return events.getContent()
                .stream()
                .map(this::mapEventDbToEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<EventDb> events = this.dbEventRepository.findByDateBetween(start, end, pageable);
        return events.getContent()
                .stream()
                .map(this::mapEventDbToEvent)
                .collect(Collectors.toList());
    }

    @Override
    public Event createEvent(Event event) {
        if (event == null) {
            LOGGER.log(Level.WARN, "Can not to create an event: {}", event);
            return null;
        }
        EventDb eventDb = mapEventToEventDb(event);
        eventDb.setId(null);
        this.dbEventRepository.save(eventDb);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event == null) {
            LOGGER.log(Level.WARN, "Can not to create an event: {}", event);
            return null;
        }
        EventDb eventDb = mapEventToEventDb(event);
        this.dbEventRepository.save(eventDb);
        return event;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        Optional<EventDb> eventDbOptional = this.dbEventRepository.findById(eventId);
        if (eventDbOptional.isPresent()) {
            this.dbEventRepository.deleteById(eventId);
            LOGGER.log(Level.DEBUG, "Successfully deletion of the event with id: {}", eventId);
            return true;
        } else {
            LOGGER.log(Level.WARN, "Can not to delete an event with id: {}", eventId);
            return false;
        }
    }

    private Event mapEventDbToEvent(EventDb eventDb) {
        return new EventImpl(eventDb.getId(), eventDb.getTitle(), eventDb.getDate(), eventDb.getTicketPrice());
    }

    private EventDb mapEventToEventDb(Event event) {
        return new EventDb(event.getId(), event.getTitle(), event.getDate(), event.getTicketPrice());
    }
}
