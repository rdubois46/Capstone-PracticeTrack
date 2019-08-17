package org.launchcode.capstonepracticetrack.controllers;


import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.PracticeChunk;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.PracticeChunkDao;
import org.launchcode.capstonepracticetrack.models.data.SessionDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("session")
public class SessionController {

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private PracticeChunkDao practiceChunkDao;

    // when user clicks "create session" from profile, direct to this controller
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String createSession(Model model, @PathVariable int id) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        Iterable<Skill> givenSkills = currentInstrument.getSkills();
        int userId = currentInstrument.getUser().getId();

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("userId", userId);

        return "session/add-session";
    }

    // when user makes selections for how to enter their practice data, direct to this controller
    @RequestMapping(value = "data-entry/{id}", method = RequestMethod.POST)
    public String processUserDataEntryChoices(Model model, @PathVariable int id, @RequestParam String skillChoice, @RequestParam String timeChoice) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        int userId = currentInstrument.getUser().getId();
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        ArrayList<PracticeChunk> chunkList = new ArrayList<PracticeChunk>();

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("userId", userId);
        model.addAttribute("skillChoice", skillChoice);
        model.addAttribute("timeChoice", timeChoice);

        model.addAttribute("skill", new Skill());
        model.addAttribute("practiceChunk", new PracticeChunk());
        model.addAttribute("currentSession", new Session());
        model.addAttribute("chunkList", chunkList);

        return "session/data-entry";
    }

    // Case 1: User selected manual skill entry and manual time entry
    @RequestMapping(value = "data-entry-validation/manual-manual/{id}", method = RequestMethod.POST)
    public String validatePracticeDataCase1(Model model, @PathVariable int id, @RequestParam String enteredSkill, @RequestParam String enteredTime, @RequestParam ArrayList<PracticeChunk> chunkList) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        int userId = currentInstrument.getUser().getId();
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        if ( enteredSkill.length() < 1 || enteredSkill.length() > 26) {
            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("skillError", "The skill must be 1-25 characters in length.");
            model.addAttribute("chunkList", chunkList);

            return "session/data-entry";
        } else {

            int enteredTimeInt = Integer.parseInt(enteredTime);


            // Create and save the manually added skill
            Skill newSkill = new Skill(enteredSkill, currentInstrument);
            skillDao.save(newSkill);


           // Chunk created but not yet assigned to Session; session not created until user finalizes/saves Session
            PracticeChunk newChunk = new PracticeChunk();
            newChunk.setSkill(newSkill);
            newChunk.setTimeInMinutes(enteredTimeInt);

            // Running list to track added Chunks before user elects to finalize/save Session
            chunkList.add(newChunk);


            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("chunkList", chunkList);

            return "session/data-entry";
        }

    }

}
