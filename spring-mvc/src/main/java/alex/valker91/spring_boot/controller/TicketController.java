package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("list")
    public String getListTicketsPage() {
        return "users/list_tickets";
    }
}
