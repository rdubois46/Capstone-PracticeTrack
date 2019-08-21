package org.launchcode.capstonepracticetrack.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Session implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Instrument instrument;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private List<PracticeChunk> practiceChunks = new ArrayList<>();

    public Session (LocalDate date, User user, Instrument instrument) {
        this.date = date;
        this.user = user;
        this.instrument = instrument;
    }

    public Session () {

    }

    // returns list of Skill objects practiced in this Session

    public ArrayList<Skill> getSkillsPracticed () {
        ArrayList<Skill> skillList = new ArrayList<>();

        for (PracticeChunk chunk : this.getPracticeChunks()) {
            Skill givenSkill = chunk.getSkill();
            if ( !Arrays.asList(skillList).contains(givenSkill)) {
                skillList.add(givenSkill);
            }
        }
        return skillList;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public List<PracticeChunk> getPracticeChunks() {
        return practiceChunks;
    }

    public void setPracticeChunks(List<PracticeChunk> practiceChunks) {
        this.practiceChunks = practiceChunks;
    }
}
