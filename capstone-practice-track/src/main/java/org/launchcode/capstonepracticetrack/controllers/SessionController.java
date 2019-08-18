package org.launchcode.capstonepracticetrack.controllers;


import org.launchcode.capstonepracticetrack.models.*;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.PracticeChunkDao;
import org.launchcode.capstonepracticetrack.models.data.SessionDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
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
    public String processUserDataEntryChoices(Model model, @PathVariable int id, @RequestParam String skillChoice, @RequestParam String timeChoice, HttpSession session) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        int userId = currentInstrument.getUser().getId();
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        int testId = (int) session.getAttribute("user");

        // create "running" practice chunk list to be stored in HttpSession until user decides to finalize/save practice session
        ArrayList<PracticeChunk> chunkList = new ArrayList<>();
        session.setAttribute("chunkList", chunkList);
        // .getAttribute returns type "Obj"; here we have to cast it to the desired type "ArrayList<PracticeChunk>"
        chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");


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
    public String validatePracticeDataCase1(Model model, @PathVariable int id, @RequestParam String enteredSkill, @RequestParam String enteredTime, HttpSession session) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        int userId = currentInstrument.getUser().getId();
        Iterable<Skill> givenSkills = currentInstrument.getSkills();


        if ( enteredSkill.length() < 1 || enteredSkill.length() > 26) {

            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
            session.setAttribute("chunkList", chunkList);

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("skillError", "The skill must be 1-25 characters in length.");
            model.addAttribute("chunkList", chunkList);

            return "session/data-entry";
        }

        if (enteredTime == null || enteredTime.equals("") || Integer.parseInt(enteredTime) < 1) {

            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
            session.setAttribute("chunkList", chunkList);

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("timeError", "Time entered must be at least 1.");
            model.addAttribute("chunkList", chunkList);
            model.addAttribute("enteredSkill", enteredSkill);

            return "session/data-entry";

        } else {

            // PracticeChunk class will require the time to be converted to "int"
            int enteredTimeInt = Integer.parseInt(enteredTime);


            // Create and save the manually added skill
            Skill newSkill = new Skill(enteredSkill, currentInstrument);
            skillDao.save(newSkill);

           // Chunk created but not yet assigned to Session; session not created until user finalizes/saves Session
            PracticeChunk newChunk = new PracticeChunk();
            newChunk.setSkill(newSkill);
            newChunk.setTimeInMinutes(enteredTimeInt);

            // Pull running chunkList from session, add the new chunk, put updated list back in session
            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
            chunkList.add(newChunk);
            session.setAttribute("chunkList", chunkList);

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

    // Case 2: User wants list-based skill entry and manual time entry
    @RequestMapping(value = "data-entry-validation/list-manual/{id}", method = RequestMethod.POST)
    public String validatePracticeDataCase2(@ModelAttribute @Valid PracticeChunk newChunk, Errors errors, Model model, @PathVariable int id, @RequestParam int skillId, HttpSession session) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        int userId = currentInstrument.getUser().getId();
        Iterable<Skill> givenSkills = currentInstrument.getSkills();



        if (errors.hasErrors()) {
            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
            session.setAttribute("chunkList", chunkList);

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("skillChoice", "list");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("chunkList", chunkList);

            return "session/data-entry";

        }
            // PracticeChunk class will require the time to be converted to "int"
            /*int enteredTimeInt = Integer.parseInt(enteredTime);*/


            // Chunk created but not yet assigned to Session; session not created until user finalizes/saves Session
        Skill selectedSkill = skillDao.findOne(skillId);
        newChunk.setSkill(selectedSkill);

            // Pull running chunkList from session, add the new chunk, put updated list back in session
        ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
        chunkList.add(newChunk);
        session.setAttribute("chunkList", chunkList);

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("userId", userId);
        model.addAttribute("skillChoice", "list");
        model.addAttribute("timeChoice", "manual");
        model.addAttribute("chunkList", chunkList);

        return "session/data-entry";

    }

    @RequestMapping(value = "save/{id}", method = RequestMethod.POST)
    public String saveSession(Model model, @PathVariable int id, HttpSession session) {

        Session newSession = new Session();

        Instrument sessionInstrument = instrumentDao.findOne(id);
        User sessionUser = sessionInstrument.getUser();
        ArrayList<PracticeChunk> sessionChunks = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");

        newSession.setInstrument(sessionInstrument);
        newSession.setUser(sessionUser);
        newSession.setPracticeChunks(sessionChunks);

        sessionDao.save(newSession);



        model.addAttribute("title", "Session Saved!");
        model.addAttribute("userId", session.getAttribute("user"));

        return "session/confirmation";
    }


}
