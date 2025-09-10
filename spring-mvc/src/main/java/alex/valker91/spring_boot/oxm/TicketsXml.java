package alex.valker91.spring_boot.oxm;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "tickets")
public class TicketsXml {

    private List<TicketXml> tickets = new ArrayList<>();

    @XmlElement(name = "ticket")
    public List<TicketXml> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketXml> tickets) {
        this.tickets = tickets;
    }
}
