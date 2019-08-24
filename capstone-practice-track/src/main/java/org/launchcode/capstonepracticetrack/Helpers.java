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
    public static List<Skill> getSkillsPracticed (Session givenSession) {
        List<Skill> skillList = new ArrayList<>();

        for (PracticeChunk chunk : givenSession.getPracticeChunks()) {
            Skill givenSkill = chunk.getSkill();
            if ( !skillList.contains(givenSkill)) {
                skillList.add(givenSkill);
            }
        }
        return skillList;
    }

    // returns list of all unique Skill objects practiced across a selected group of Sessions
    public static List<Skill> getAllSkillsPracticed(List<Session> selectedSessions) {
        List<Skill> allSkillsList = new ArrayList<>();

        for (Session givenSession : selectedSessions) {
            List<Skill> skillIdList = Helpers.getSkillsPracticed(givenSession);
            for (Skill givenSkill : skillIdList) {
                if (!allSkillsList.contains(givenSkill)) {
                    allSkillsList.add(givenSkill);
                }

            }
        }

        return allSkillsList;
    }

    // returns list of unique skill ids practiced during this Session
    public static List<Integer> getSkillIds(Session givenSession) {
        List<Integer> skillIdList = new ArrayList<>();
        List<Skill> skillList = Helpers.getSkillsPracticed(givenSession);

        for (Skill skill : skillList) {
            Integer id = skill.getId();
            skillIdList.add(id);
        }

        return skillIdList;
    }

    // returns list of all unique skill ids practiced across a selected group of Sessions
    public static List<Integer> getAllSkillsIdsPracticed(List<Session> selectedSessions) {
        List<Integer> allSkillIdsList = new ArrayList<>();

        for (Session givenSession : selectedSessions) {
            List<Integer> skillIdList = Helpers.getSkillIds(givenSession);
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

    // This takes a list of practice Sessions and returns a list containing one "Row" object for every unique Skill
    // that was practiced, even just once, across all Sessions. These rows are used in the Practice Data view to
    // display a table.
    public static List<SkillDataRow> createSkillDataRows(List<Session> givenSessions) {
        List<SkillDataRow> skillDataRowsList = new ArrayList<>();
        List<Skill> allSkillsAcrossGivenSessions = Helpers.getAllSkillsPracticed(givenSessions);

        for (Skill givenSkill : allSkillsAcrossGivenSessions) {
            SkillDataRow skillRow  = new SkillDataRow();
            ArrayList<HashMap<Session, Integer>> rowChunks = new ArrayList<>();

            for (Session session : givenSessions) {
                HashMap<Session, Integer> sessionToTimeMap = new HashMap<>();
                sessionToTimeMap.put(session, 0);

                List<PracticeChunk> sessionChunks = session.getPracticeChunks();
                for (PracticeChunk chunk : sessionChunks) {

                    if (givenSkill.equals(chunk.getSkill())) {
                        Integer previousTime = sessionToTimeMap.get(session);
                        Integer additionalTime = chunk.getTimeInMinutes();
                        Integer newTime = previousTime + additionalTime;
                        sessionToTimeMap.put(session, newTime);
                    }
                }
                rowChunks.add(sessionToTimeMap);
            }
            skillRow.setSkill(givenSkill);
            skillRow.setSkillChunks(rowChunks);

            skillDataRowsList.add(skillRow);
        }

        return skillDataRowsList;
    }

    // This one was totally borrowed from StackOverflow. Don't @ me
    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }



}




