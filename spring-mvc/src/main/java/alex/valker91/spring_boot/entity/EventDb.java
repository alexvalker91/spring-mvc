package alex.valker91.spring_boot.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(schema = "public", name = "event_db")
public class EventDb {

    public EventDb() {}

    public EventDb(long id, String title, Date date, int ticketPrice) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title")
    private String title;

    @Column(name = "event_date")
    private Date date;

    @Column(name = "event_ticket_price")
    private int ticketPrice;

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Date getDate() {
        return date;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}

//CREATE TABLE event_db (
//        id SERIAL PRIMARY KEY,
//        event_title VARCHAR(255) NOT NULL,
//event_date TIMESTAMP NOT NULL,
//event_ticket_price INTEGER
//);

//INSERT INTO event_db (event_title, event_date, event_ticket_price)
//VALUES ('My First Event', '2023-10-05 18:00:00', 100);