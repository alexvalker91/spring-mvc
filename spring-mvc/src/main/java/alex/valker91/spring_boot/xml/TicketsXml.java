package alex.valker91.spring_boot.xml;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsXml {

    @XmlElement(name = "ticket")
    private List<TicketXml> tickets = new ArrayList<>();

    public List<TicketXml> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketXml> tickets) {
        this.tickets = tickets;
    }
}

