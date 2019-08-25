package org.launchcode.capstonepracticetrack;

import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.launchcode.capstonepracticetrack.models.PracticeSession;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.PracticeSessionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class Helpers {

    @Autowired
    private PracticeSessionDao practiceSessionDao;

    // returns list of unique Skill objects practiced in this Session
    public static List<Skill> getSkillsPracticed (PracticeSession givenPracticeSession) {
        List<Skill> skillList = new ArrayList<>();

        for (PracticeChunk chunk : givenPracticeSession.getPracticeChunks()) {
            Skill givenSkill = chunk.getSkill();
            if ( !skillList.contains(givenSkill)) {
                skillList.add(givenSkill);
            }
        }
        return skillList;
    }

    // returns list of all unique Skill objects practiced across a selected group of Sessions
    public static List<Skill> getAllSkillsPracticed(List<PracticeSession> selectedPracticeSessions) {
        List<Skill> allSkillsList = new ArrayList<>();

        for (PracticeSession givenPracticeSession : selectedPracticeSessions) {
            List<Skill> skillIdList = Helpers.getSkillsPracticed(givenPracticeSession);
            for (Skill givenSkill : skillIdList) {
                if (!allSkillsList.contains(givenSkill)) {
                    allSkillsList.add(givenSkill);
                }

            }
        }

        return allSkillsList;
    }

    // returns list of unique skill ids practiced during this Session
    public static List<Integer> getSkillIds(PracticeSession givenPracticeSession) {
        List<Integer> skillIdList = new ArrayList<>();
        List<Skill> skillList = Helpers.getSkillsPracticed(givenPracticeSession);

        for (Skill skill : skillList) {
            Integer id = skill.getId();
            skillIdList.add(id);
        }

        return skillIdList;
    }

    // returns list of all unique skill ids practiced across a selected group of Sessions
    public static List<Integer> getAllSkillsIdsPracticed(List<PracticeSession> selectedPracticeSessions) {
        List<Integer> allSkillIdsList = new ArrayList<>();

        for (PracticeSession givenPracticeSession : selectedPracticeSessions) {
            List<Integer> skillIdList = Helpers.getSkillIds(givenPracticeSession);
            for (Integer givenId : skillIdList) {
                if (!allSkillIdsList.contains(givenId)) {
                    allSkillIdsList.add(givenId);
                }

            }
        }

        return allSkillIdsList;
    }

    // used to get specified number of sessions from existing list of sessions
    public static List<PracticeSession> limitPracticeSessionsBy(List<PracticeSession> givenPracticeSessions, int count) {
        if (givenPracticeSessions.isEmpty()) {
            return null;
        }

        List<PracticeSession> selectedPracticeSessions = new ArrayList<PracticeSession>();

        for (int i = 0; i < count; i++) {
            selectedPracticeSessions.add(givenPracticeSessions.get(i));

        }
        Collections.reverse(selectedPracticeSessions);
        return selectedPracticeSessions;
    }

    // This takes a list of practice Sessions and returns a list containing one "Row" object for every unique Skill
    // that was practiced, even just once, across all Sessions. These rows are used in the Practice Data view to
    // display a table.
    public static List<SkillDataRow> createSkillDataRows(List<PracticeSession> givenPracticeSessions) {
        List<SkillDataRow> skillDataRowsList = new ArrayList<>();
        List<Skill> allSkillsAcrossGivenPracticeSessions = Helpers.getAllSkillsPracticed(givenPracticeSessions);

        for (Skill givenSkill : allSkillsAcrossGivenPracticeSessions) {
            SkillDataRow skillRow  = new SkillDataRow();
            ArrayList<HashMap<PracticeSession, Integer>> rowChunks = new ArrayList<>();

            for (PracticeSession practiceSession : givenPracticeSessions) {
                HashMap<PracticeSession, Integer> practiceSessionToTimeMap = new HashMap<>();
                practiceSessionToTimeMap.put(practiceSession, 0);

                List<PracticeChunk> practiceSessionChunks = practiceSession.getPracticeChunks();
                for (PracticeChunk chunk : practiceSessionChunks) {

                    if (givenSkill.equals(chunk.getSkill())) {
                        Integer previousTime = practiceSessionToTimeMap.get(practiceSession);
                        Integer additionalTime = chunk.getTimeInMinutes();
                        Integer newTime = previousTime + additionalTime;
                        practiceSessionToTimeMap.put(practiceSession, newTime);
                    }
                }
                rowChunks.add(practiceSessionToTimeMap);
            }
            skillRow.setSkill(givenSkill);
            skillRow.setSkillChunks(rowChunks);

            skillDataRowsList.add(skillRow);
        }

        return skillDataRowsList;
    }

    // This method courtesy of the good folks at Stack Overflow. Thank you for sharing your knowledge.
    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }



}




