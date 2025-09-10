package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.controller.payload.NewTicketPayload;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.impl.EventImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(("tickets"))
@RequiredArgsConstructor
public class TicketController {

    private final BookingFacade bookingFacade;

    @GetMapping("main/page")
    public String getTicketsMainPage() {
        return "tickets/main_page";
    }

    @GetMapping("create")
    public String getCreateTicketPage() {
        return "tickets/new_ticket";
    }

    @GetMapping("list")
    public String getListTicketsPage() {
        return "tickets/list_tickets";
    }

    @PostMapping("create")
    public String createTicket(NewTicketPayload payload,
                               Model model,
                               HttpServletResponse response) {
        try {
            Ticket ticket = bookingFacade.bookTicket(payload.userId(), payload.eventId(), payload.place(), payload.category());
            if (ticket == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("payload", payload);
                model.addAttribute("errors", "Unable to book ticket. Check balance or place availability.");
                return "tickets/new_ticket";
            }
            model.addAttribute("ticket", ticket);
            return "tickets/new_ticket";
        } catch (Exception exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getMessage());
            return "tickets/new_ticket";
        }
    }

    @GetMapping("cancel")
    public String getCancelTicketPage() {
        return "tickets/cancel_ticket";
    }

    @PostMapping("cancel")
    public String cancelTicket(@RequestParam("ticketId") long ticketId,
                               Model model) {
        boolean result = bookingFacade.cancelTicket(ticketId);
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("result", result);
        return "tickets/cancel_ticket";
    }

    @GetMapping("list/by-event")
    public String getTicketsByEvent(@RequestParam("eventId") long eventId,
                                    @RequestParam("title") String title,
                                    @RequestParam("date") String date,
                                    @RequestParam("ticketPrice") int ticketPrice,
                                    @RequestParam("pageSize") int pageSize,
                                    @RequestParam("pageNum") int pageNum,
                                    Model model,
                                    HttpServletResponse response) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = formatter.parse(date);
            Event event = new EventImpl(eventId, title, parsedDate, ticketPrice);

            List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);

            model.addAttribute("eventId", eventId);
            model.addAttribute("title", title);
            model.addAttribute("date", date);
            model.addAttribute("ticketPrice", ticketPrice);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("ticketsByEvent", tickets);
        } catch (ParseException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("eventId", eventId);
            model.addAttribute("title", title);
            model.addAttribute("date", date);
            model.addAttribute("ticketPrice", ticketPrice);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pageNum", pageNum);
        }
        return "tickets/list_tickets";
    }
}
