package org.launchcode.capstonepracticetrack.models;



import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 15, message = "Username must be 5 - 15 characters." )
    private String username;

    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be 5 - 15 characters.")
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Instrument> instruments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Session> sessions = new ArrayList<>();

    public User () {

    }

    public User (String username, String password) {
        this.username = username;
    }


    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}




