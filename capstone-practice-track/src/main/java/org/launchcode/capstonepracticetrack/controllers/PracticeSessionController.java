package org.launchcode.capstonepracticetrack.controllers;


import org.apache.commons.lang3.time.StopWatch;
import org.launchcode.capstonepracticetrack.Helpers;
import org.launchcode.capstonepracticetrack.models.*;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.PracticeChunkDao;
import org.launchcode.capstonepracticetrack.models.data.PracticeSessionDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("practice-session")
public class PracticeSessionController extends AbstractBaseController {

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private PracticeSessionDao practiceSessionDao;

    @Autowired
    private PracticeChunkDao practiceChunkDao;


    // when user clicks "create session" from profile, direct to this controller
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createPracticeSession(Model model, String instrumentId) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findById(instrId);

        Iterable<Skill> givenSkills = currentInstrument.getSkills();
        int userId = currentInstrument.getUser().getId();

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("date", LocalDate.now());

        return "practice-session/add-session";
    }



    // when user makes selections for how to enter their practice data, direct to this controller
    @RequestMapping(value = "data-entry", method = RequestMethod.POST)
    public String processPracticeSessionDataEntryChoices(Model model, @RequestParam String skillChoice, @RequestParam String timeChoice, @RequestParam String date, String instrumentId, HttpSession session) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findById(instrId);
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        // if user chooses to use their existing skill list, but nothing is in it, returns to previous view with error message
        if (skillChoice.equals("list") && ((List<Skill>) givenSkills).isEmpty()) {

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("noSkillsError", "You don't have any skills in your list yet. Select manual entry or add skills in your profile.");

            return "practice-session/add-session";
        }

        // put date in http session; will be used later as field in PracticeSession when user finalizes/saves PracticeSession
        // if date is invalid, return page with error message
        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("date", LocalDate.now());
            model.addAttribute("dateError", "Not a valid date.");

            return "practice-session/add-session";
        }

        session.setAttribute("practiceSessionDate", localDate);

        // create "running" practice chunk list to be stored in HttpSession until user decides to finalize/save practice session
        ArrayList<PracticeChunk> chunkList = new ArrayList<>();
        session.setAttribute("chunkList", chunkList);

        // .getAttribute returns type "Obj"; here we have to cast it to the desired type "ArrayList<PracticeChunk>"
        chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");


        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("skillChoice", skillChoice);
        model.addAttribute("timeChoice", timeChoice);
        model.addAttribute("newChunk", new PracticeChunk());
        model.addAttribute("chunkList", chunkList);

        return "practice-session/data-entry";
    }



    // Case 1: User selected manual skill entry and manual time entry
    @RequestMapping(value = "data-entry-validation/manual-manual", method = RequestMethod.POST)
    public String validatePracticeDataCase1(Model model, @RequestParam String enteredSkill, @RequestParam String enteredTime, String instrumentId, HttpSession session) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findOne(instrId);

        // validate manually entered skill name
        if ( enteredSkill.length() < 1 || enteredSkill.length() > 26) {

            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("skillError", "The skill must be 1-25 characters in length.");
            model.addAttribute("chunkList", chunkList);

            return "practice-session/data-entry";
        }

        // validate manually entered time
        if (enteredTime == null || enteredTime.equals("") || Integer.parseInt(enteredTime) < 1) {

            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("timeError", "Time entered must be at least 1.");
            model.addAttribute("chunkList", chunkList);
            model.addAttribute("enteredSkill", enteredSkill);

            return "practice-session/data-entry";

        } else {

            // Check if Skill with same name already exists in user's list; if it does, we just use the existing Skill.
            Skill selectedSkill;
            if (Helpers.doesSkillAlreadyExist(instrId, enteredSkill, skillDao)) {
                selectedSkill = skillDao.findByInstrument_idAndName(instrId, enteredSkill);
            } else {
                // Create and save the manually added skill
                selectedSkill = new Skill(enteredSkill, currentInstrument);
                skillDao.save(selectedSkill);
            }

           // Chunk created here but not yet assigned to PracticeSession. Will be assigned when user finalizes/saves PracticeSession
            PracticeChunk newChunk = new PracticeChunk();
            newChunk.setSkill(selectedSkill);
            int enteredTimeInt = Integer.parseInt(enteredTime);
            newChunk.setTimeInMinutes(enteredTimeInt);

            // Pull running chunkList from Http session, add the new chunk, put updated list back in Http session
            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
            chunkList.add(newChunk);
            session.setAttribute("chunkList", chunkList);

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("chunkList", chunkList);

            return "practice-session/data-entry";
        }

    }



    // Case 2: User wants list-based skill entry and manual time entry
    @RequestMapping(value = "data-entry-validation/list-manual/", method = RequestMethod.POST)
    public String validatePracticeDataCase2(@ModelAttribute @Valid PracticeChunk newChunk, Errors errors, Model model, @RequestParam int skillId, String instrumentId, HttpSession session) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findOne(instrId);
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        // In this case, we can create a new PracticeChunk directly from model binding and validate with @Valid
        if (errors.hasErrors()) {
            ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");

            model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skills", givenSkills);
            model.addAttribute("skillChoice", "list");
            model.addAttribute("timeChoice", "manual");
            model.addAttribute("newChunk", newChunk);
            model.addAttribute("chunkList", chunkList);

            return "practice-session/data-entry";

        }

        // For our new PracticeChunk, the model binding assigned 'timeInMinutes' field,
        // but 'skill' field is assigned manually here as it requires an entire Skill object
        Skill selectedSkill = skillDao.findOne(skillId);
        newChunk.setSkill(selectedSkill);

        // Pull running chunkList from session, add the new chunk, put updated list back in session
        ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
        chunkList.add(newChunk);
        session.setAttribute("chunkList", chunkList);

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute("skillChoice", "list");
        model.addAttribute("timeChoice", "manual");
        model.addAttribute("newChunk", new PracticeChunk());
        model.addAttribute("chunkList", chunkList);

        return "practice-session/data-entry";

    }



    // user is ready to finalize/save their PracticeSession
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String savePracticeSession(Model model, String instrumentId, HttpSession session) {

        int instrId = Integer.parseInt(instrumentId);

        PracticeSession newPracticeSession = new PracticeSession();

        // set all PracticeSession fields and save
        LocalDate date = (LocalDate) session.getAttribute("practiceSessionDate");
        newPracticeSession.setDate(date);

        Instrument practiceSessionInstrument = instrumentDao.findOne(instrId);
        newPracticeSession.setInstrument(practiceSessionInstrument);

        User practiceSessionUser = practiceSessionInstrument.getUser();
        newPracticeSession.setUser(practiceSessionUser);

        ArrayList<PracticeChunk> practiceSessionChunks = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
        newPracticeSession.setPracticeChunks(practiceSessionChunks);

        practiceSessionDao.save(newPracticeSession);

        model.addAttribute("title", "Session Saved!");
        model.addAttribute("userId", session.getAttribute("user"));

        return "practice-session/confirmation";
    }


    //Case 3 - user wants manual skill entry with an automatic timer
    @RequestMapping(value="data-entry-validation/manual-timer", method=RequestMethod.POST)
    public String startTimer(Model model, @RequestParam String instrumentId, @RequestParam(required=false) String time, @RequestParam(required=false) String enteredSkill, @RequestParam(required=false) String skillId, HttpSession session) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findById(instrId);
        Skill selectedSkill;

        //
        if (enteredSkill != null) {
            // 'manual' skill choice requires same validation for skill as Case 1 controller
            if (enteredSkill.length() < 1 || enteredSkill.length() > 26) {

                ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");

                model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
                model.addAttribute("instrument", currentInstrument);
                model.addAttribute("skillChoice", "manual");
                model.addAttribute("timeChoice", "timer");
                model.addAttribute("skillError", "The skill must be 1-25 characters in length.");
                model.addAttribute("chunkList", chunkList);

                return "practice-session/data-entry";
            }

            // Check if Skill with same name already exists in user's list; if it does, we just use the existing Skill.
            if (Helpers.doesSkillAlreadyExist(instrId, enteredSkill, skillDao)) {
                selectedSkill = skillDao.findByInstrument_idAndName(instrId, enteredSkill);
            } else {
                // Create and save the manually added skill
                selectedSkill = new Skill(enteredSkill, currentInstrument);
                skillDao.save(selectedSkill);
            }


            selectedSkill = skillDao.findByInstrument_idAndName(instrId, selectedSkill.getName());


            model.addAttribute("title", "Time Your Practice");
            model.addAttribute("instrument", currentInstrument);
            model.addAttribute("skill", selectedSkill);
            model.addAttribute("skillChoice", "manual");
            model.addAttribute("timeChoice", "timer");
            model.addAttribute("stopwatch", true);


            return "practice-session/data-entry";
        }

        int recordedTimeInMinutes = Integer.parseInt(time);


        PracticeChunk newChunk = new PracticeChunk();
        selectedSkill = skillDao.findOne(Integer.parseInt(skillId));
        newChunk.setSkill(selectedSkill);
        newChunk.setTimeInMinutes(recordedTimeInMinutes);


        // Pull running chunkList from Http session, add the new chunk, put updated list back in Http session
        ArrayList<PracticeChunk> chunkList = (ArrayList<PracticeChunk>) session.getAttribute("chunkList");
        chunkList.add(newChunk);
        session.setAttribute("chunkList", chunkList);


        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skillChoice", "manual");
        model.addAttribute("timeChoice", "timer");
        model.addAttribute("chunkList", chunkList);


        return "practice-session/data-entry";
    }

    @RequestMapping(value = "check-timer", method = RequestMethod.POST)
    public String checkTimer(Model model, @RequestParam String instrumentId, @RequestParam int skillId, HttpSession session) {
        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findById(instrId);
        Skill selectedSkill = skillDao.findOne(skillId);

        StopWatch timer = (StopWatch) session.getAttribute("timer");
        double hours = timer.getTime(TimeUnit.HOURS);
        double minutes = timer.getTime(TimeUnit.MINUTES);
        double seconds = timer.getTime(TimeUnit.SECONDS);

        model.addAttribute("title", "Time Your Practice");
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skill", selectedSkill);
        model.addAttribute("elapsedTime", true);
        model.addAttribute("hours", hours);
        model.addAttribute("minutes", minutes);
        model.addAttribute("seconds", seconds);

        return "practice-session/timer";
    }

}
