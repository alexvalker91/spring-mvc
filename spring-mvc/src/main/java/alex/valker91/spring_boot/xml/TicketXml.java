package alex.valker91.spring_boot.xml;

import alex.valker91.spring_boot.model.Ticket;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketXml {

    @XmlAttribute(name = "user")
    private long userId;

    @XmlAttribute(name = "event")
    private long eventId;

    @XmlAttribute(name = "category")
    private Ticket.Category category;

    @XmlAttribute(name = "place")
    private int place;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Ticket.Category getCategory() {
        return category;
    }

    public void setCategory(Ticket.Category category) {
        this.category = category;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}

