package alex.valker91.spring_boot.controller.payload;

import alex.valker91.spring_boot.model.Ticket;

public record NewTicketPayload(long userId, long eventId, int place, Ticket.Category category) {
}

