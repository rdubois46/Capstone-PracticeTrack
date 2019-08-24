package org.launchcode.capstonepracticetrack;

import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillDataRow {

    private Skill skill;

    private ArrayList<HashMap<Session, Integer>> skillChunks = new ArrayList<>();

    private int total = 0;

    private double average = 0;

    // constructors
    public SkillDataRow() {

    }

    public SkillDataRow(Skill skill) {
        this.skill = skill;
    }

    public SkillDataRow(Skill skill, ArrayList<HashMap<Session, Integer>> skillChunks) {
        this.skill = skill;
        this.skillChunks = skillChunks;
        for (HashMap<Session, Integer> chunk : skillChunks) {
            for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
                this.total = this.total + entry.getValue();
            }
        }

        this.average = Helpers.round(this.total / this.skillChunks.size(), 2);
    }

    //methods
    public void addChunk(HashMap<Session, Integer> chunk) {
        this.skillChunks.add(chunk);
        for (Map.Entry<Session, Integer> entry : chunk.entrySet()) {
            this.total = this.total + entry.getValue();
        }

        this.average = Helpers.round(this.total / this.skillChunks.size(), 2);
    }

    // returns list of Integers - practice chunk times - separated from their corresponding Sessions.
    // This prevents having to iterate over HashMaps when displaying these times in the view.
    public ArrayList<Integer> getTimesList() {
        ArrayList<Integer> timesList = new ArrayList<>();

        for (HashMap<Session, Integer> chunk : this.skillChunks) {
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

        this.average = Helpers.round(this.total / this.skillChunks.size(), 2);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
