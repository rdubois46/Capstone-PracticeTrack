package org.launchcode.capstonepracticetrack.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class PracticeChunk {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Session session;

    @ManyToOne
    @NotNull
    private Skill skill;

    @NotNull
    @Size(min=1, message = "Time cannot be less than 1 minute.")
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
