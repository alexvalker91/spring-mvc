package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.controller.payload.NewTicketPayload;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Ticket;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("list")
    public String getListTicketsPage() {
        return "users/list_tickets";
    }
}
