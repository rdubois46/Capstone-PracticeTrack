package org.launchcode.capstonepracticetrack.helpers;

import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.SessionDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;

import java.util.HashMap;

public class GetSessionAndTimeMap {

    private SkillDao skillDao;

    private InstrumentDao instrumentDao;

    private SessionDao sessionDao;

    public static HashMap<Integer, Integer> getSessionAndTimeMap(Skill skill, Iterable<Session> givenSessions) {
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
    }
}
