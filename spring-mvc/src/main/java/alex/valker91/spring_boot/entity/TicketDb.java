package alex.valker91.spring_boot.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "ticket_db")
public class TicketDb {

    public TicketDb() {}

    public TicketDb(long id, long eventId, long userId, Category category, int place) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.category = category;
        this.place = place;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "user_id")
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "ticket_place")
    private int place;

    public enum Category {STANDARD, PREMIUM, BAR}

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}

//CREATE TYPE ticket_category AS ENUM ('STANDARD', 'PREMIUM', 'BAR');

//CREATE TABLE ticket_db (
//        id SERIAL PRIMARY KEY,
//        event_id INT REFERENCES event_db(id),
//user_id INT REFERENCES user_db(id),
//category ticket_category,
//ticket_place INTEGER
//);