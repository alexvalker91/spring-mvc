package alex.valker91.spring_boot;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private BookingFacade bookingFacade;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World");
        Event event = bookingFacade.getEventById(1);
        System.out.println("Title Kurami: " + event.getTitle());
        System.out.println("Hello World");
        bookingFacade.preloadTickets(new ClassPathResource("tickets1.xml"));
    }
}
