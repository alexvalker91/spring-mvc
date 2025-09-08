package alex.valker91.spring_boot.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "user_db")
public class UserDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    public UserDb() {}

    public UserDb(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

//CREATE TABLE user_db (
//        id SERIAL PRIMARY KEY,
//        user_name VARCHAR(255) NOT NULL,
//user_email VARCHAR(255) NOT NULL
//);