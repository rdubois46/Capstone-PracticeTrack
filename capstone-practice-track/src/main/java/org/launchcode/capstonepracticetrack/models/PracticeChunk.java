package org.launchcode.capstonepracticetrack.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class PracticeChunk implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Session session;

    @ManyToOne
    private Skill skill;

    @NotNull(message = "Can't be empty.")
    @Min(value = 1, message = "Time must be at least 1 minute.")
    @Max(value = 1000, message = "Time must be less than 1000 minutes. You're not a robot.")
    private int timeInMinutes;

    public PracticeChunk() {

    }

    public int getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }
}
