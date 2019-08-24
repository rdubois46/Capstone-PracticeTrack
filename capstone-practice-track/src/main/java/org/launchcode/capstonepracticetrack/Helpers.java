package org.launchcode.capstonepracticetrack;

import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.*;

public class Helpers {

    @Autowired
    private SessionDao sessionDao;

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

    // used to get specified number of sessions from existing list of sessions
    public static List<Session> limitSessionsBy(List<Session> givenSessions, int count) {
        if (givenSessions.isEmpty()) {
            return null;
        }

        List<Session> selectedSessions = new ArrayList<Session>();

        for (int i = 0; i < count; i++) {
            selectedSessions.add(givenSessions.get(i));

        }
        Collections.reverse(selectedSessions);
        return selectedSessions;
    }



}




