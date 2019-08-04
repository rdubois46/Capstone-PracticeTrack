package org.launchcode.capstonepracticetrack.models;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instrument {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=1, max=25, message = "Instrument must be between 1 and 25 characters.")
    private String name;

    @NotNull
    private Boolean active = true;

    @ManyToOne
    private User user;

    @OneToMany
    @JoinColumn(name = "instrument_id")
    private List<Skill> skills = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "instrument_id")
    private List<Session> sessions = new ArrayList<>();

    public Instrument(String name, User user) {
        this.name = name;
        this.user = user;
        this.active = true;
    }

    public Instrument () {
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
