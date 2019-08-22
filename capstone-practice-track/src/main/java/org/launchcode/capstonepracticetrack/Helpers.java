package org.launchcode.capstonepracticetrack;

import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Helpers {

    // returns list of unique Skill objects practiced in this Session
    public static ArrayList<Skill> getSkillsPracticed (Session givenSession) {
        ArrayList<Skill> skillList = new ArrayList<>();

        for (PracticeChunk chunk : givenSession.getPracticeChunks()) {
            Skill givenSkill = chunk.getSkill();
            if ( !skillList.contains(givenSkill)) {
                skillList.add(givenSkill);
            }
        }
        return skillList;
    }

    // returns list of all unique Skill objects practiced across a selected group of Sessions
    public static ArrayList<Skill> getAllSkillsPracticed(ArrayList<Session> selectedSessions) {
        ArrayList<Skill> allSkillsList = new ArrayList<>();

        for (Session givenSession : selectedSessions) {
            ArrayList<Skill> skillIdList = Helpers.getSkillsPracticed(givenSession);
            for (Skill givenSkill : skillIdList) {
                if (!allSkillsList.contains(givenSkill)) {
                    allSkillsList.add(givenSkill);
                }

            }
        }

        return allSkillsList;
    }

    // returns list of unique skill ids practiced during this Session
    public static ArrayList<Integer> getSkillIds(Session givenSession) {
        ArrayList<Integer> skillIdList = new ArrayList<>();
        ArrayList<Skill> skillList = Helpers.getSkillsPracticed(givenSession);

        for (Skill skill : skillList) {
            Integer id = skill.getId();
            skillIdList.add(id);
        }

        return skillIdList;
    }

    // returns list of all unique skill ids practiced across a selected group of Sessions
    public static ArrayList<Integer> getAllSkillsIdsPracticed(ArrayList<Session> selectedSessions) {
        ArrayList<Integer> allSkillIdsList = new ArrayList<>();

        for (Session givenSession : selectedSessions) {
            ArrayList<Integer> skillIdList = Helpers.getSkillIds(givenSession);
            for (Integer givenId : skillIdList) {
                if (!allSkillIdsList.contains(givenId)) {
                    allSkillIdsList.add(givenId);
                }

            }
        }

        return allSkillIdsList;
    }



}

    /*public static HashMap<Integer, Integer> getSessionAndTimeMap(Skill skill, Iterable<Session> givenSessions) {
        HashMap<Integer, Integer> sessionAndTimeMap = new HashMap<>();
        int skillId = skill.getId();

        int sessionCounter = 0;
        int totalTime = 0;

        // for each session in our selection of sessions
        for (Session session : givenSessions) {
            // for each practice chunk in a single session
            for (PracticeChunk chunk : session.getPracticeChunks()) {
                // if that chunk contains the skill we want to chart, add the session Id and time to map
                if (chunk.getSkill().getId() == skillId ) {
                    sessionAndTimeMap.put(session.getId(), chunk.get)

                }
            }

        }

        return sessionAndTimeMap;
    }*/


