package alex.valker91.spring_boot.controller.payload;

import java.util.Date;

public record NewEventPayload(long id, String title, String date, int ticketPrice) {
}
