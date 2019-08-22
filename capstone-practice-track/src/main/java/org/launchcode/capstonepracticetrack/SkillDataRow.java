package org.launchcode.capstonepracticetrack;

import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillDataRow {

    private Skill skill;

    private ArrayList<HashMap<Session, Integer>> skillChunks;

    private int total;

    private int average;

    // constructors
    public SkillDataRow() {
        this.total = 0;
        for (HashMap<Session, Integer> chunk : skillChunks) {
            for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
                this.total = this.total + entry.getValue();
            }
        }

    }

    public SkillDataRow(Skill skill) {
        this.skill = skill;
        this.total = 0;
        this.average = 0;
    }

    public SkillDataRow(Skill skill, ArrayList<HashMap<Session, Integer>> skillChunks) {
        this.skill = skill;
        this.skillChunks = skillChunks;
        this.total = 0;
        for (HashMap<Session, Integer> chunk : skillChunks) {
            for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
                this.total = this.total + entry.getValue();
            }
        }

        this.average = this.total / this.skillChunks.size();
    }

    //methods
    public void addChunk(HashMap<Session, Integer> chunk) {
        this.skillChunks.add(chunk);
        for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
            this.total = this.total + entry.getValue();
        }

        this.average = this.total / this.skillChunks.size();
    }

    // returns arraylist of practice chunk times (separated from their corresponding session
    // IDs). This allows for easier iteration when displaying these times in the view
    public ArrayList<Integer> getTimesList(ArrayList<HashMap<Session, Integer>> skillChunks) {
        ArrayList<Integer> timesList = new ArrayList<>();

        for (HashMap<Session, Integer> chunk : skillChunks) {
            for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
                timesList.add(entry.getValue());
            }
        }

        return timesList;
    }


    //getters & setters
    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public ArrayList<HashMap<Session, Integer>> getSkillChunks() {
        return skillChunks;
    }

    public void setSkillChunks(ArrayList<HashMap<Session, Integer>> skillChunks) {
        this.skillChunks = skillChunks;

        for (HashMap<Session, Integer> chunk : skillChunks) {
            for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
                this.total = this.total + entry.getValue();
            }
        }

        this.average = this.total / this.skillChunks.size();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
