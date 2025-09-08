package alex.valker91.spring_boot.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "user_account_db")
public class UserAccountDb {

    public UserAccountDb() {}

    public UserAccountDb(Long id, long userId, int amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_amount")
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }
}

//CREATE TABLE user_account_db (
//        id SERIAL PRIMARY KEY,
//user_id INT REFERENCES user_db(id),
//user_amount INTEGER
//);