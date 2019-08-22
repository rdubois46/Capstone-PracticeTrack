package org.launchcode.capstonepracticetrack.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Skill implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=1, max=25, message = "Must be between 1 and 25 characters.")
    private String name;

    @NotNull
    private boolean active = true;

    @ManyToOne
    private Instrument instrument;

    @OneToMany
    @JoinColumn(name = "skill_id")
    private List<PracticeChunk> practiceChunks = new ArrayList<>();

    public Skill (String name, Instrument instrument) {
        this.name = name;
        this.instrument = instrument;
        this.active = true;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        {
            object = (Skill) object;
            if (object == this || this.id == ((Skill) object).getId()) {
                return true;
            }
        }
        return false;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
