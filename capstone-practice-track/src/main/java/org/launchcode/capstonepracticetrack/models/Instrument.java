package org.launchcode.capstonepracticetrack.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instrument implements Serializable {

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
    private List<PracticeSession> practiceSessions = new ArrayList<>();

    public Instrument(String name, User user) {
        this.name = name;
        this.user = user;
        this.active = true;
    }

    public Instrument () {
        this.active = true;
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
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

    public List<PracticeSession> getPracticeSessions() {
        return practiceSessions;
    }

    public void setPracticeSessions(List<PracticeSession> practiceSessions) {
        this.practiceSessions = practiceSessions;
    }

}
