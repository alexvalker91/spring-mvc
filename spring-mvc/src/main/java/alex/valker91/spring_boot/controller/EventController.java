package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.controller.payload.NewEventPayload;
import alex.valker91.spring_boot.controller.payload.UpdateEventPayload;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.impl.EventImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(("events"))
@RequiredArgsConstructor
public class EventController {

    private final BookingFacade bookingFacade;

    @GetMapping("main/page")
    public String getEventsMainPage() {
        return "events/main_page";
    }

    @GetMapping("create")
    public String getCreateEventPage() {
        return "events/new_event";
    }

    @PostMapping("create")
    public String createEvent(NewEventPayload payload,
                              Model model,
                              HttpServletResponse response) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Event event = new EventImpl(payload.id(), payload.title(), formatter.parse(payload.date()), payload.ticketPrice());
            event = bookingFacade.createEvent(event);
            return "redirect:/events/%d".formatted(event.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "events/new_event";
        } catch (ParseException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getMessage());
            return "events/new_event";
        }
    }

    @GetMapping("{eventId}")
    public String getEvent(@PathVariable("eventId") int eventId, Model model) {
        Event event = bookingFacade.getEventById(eventId);
        model.addAttribute("event", event);
        return "events/event";
    }

    @PostMapping("{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") int eventId) {
        boolean isDeleted = bookingFacade.deleteEvent(eventId);
        return "redirect:/events/main/page";
    }

    @GetMapping("{eventId}/edit")
    public String getEventEditPage(@PathVariable("eventId") int eventId, Model model) {
        Event event = bookingFacade.getEventById(eventId);
        model.addAttribute("event", event);
        return "events/edit";
    }

    @PostMapping("{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") int eventId,
                              UpdateEventPayload payload,
                              Model model,
                              HttpServletResponse response) {
        try {
            Event event = bookingFacade.getEventById(eventId);
            event.setTitle(payload.title());
            Event updatedEvent = bookingFacade.updateEvent(event);
            return "redirect:/events/%d".formatted(updatedEvent.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "events/edit";
        }
    }
}
