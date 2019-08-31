package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.Helpers;
import org.launchcode.capstonepracticetrack.SkillDataRow;
import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.PracticeSession;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.PracticeSessionDao;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value="records")
public class RecordsController extends AbstractBaseController {

    @Autowired
    InstrumentDao instrumentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PracticeSessionDao practiceSessionDao;


    @RequestMapping(value="/", method = RequestMethod.POST)
    public String recordsSelect(Model model, String instrumentId, HttpSession httpSession) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findOne(instrId);

        User currentUser = currentInstrument.getUser();
        int userId = currentUser.getId();

        Iterable<Instrument> allUserInstruments = instrumentDao.findByUser_id(userId);
        Iterable<PracticeSession> currentPracticeSessions =  practiceSessionDao.findByInstrument_id(instrId);

        /*httpSession.setAttribute("currentPracticeSessions", currentPracticeSessions);
        httpSession.setAttribute("currentUser", currentUser);
        httpSession.setAttribute("currentInstrument", currentInstrument);
        httpSession.setAttribute("allUserInstruments", allUserInstruments);*/


        model.addAttribute("title", "Select number of sessions to view for " + currentInstrument.getName() + ": ");
        model.addAttribute("currentPracticeSessions", currentPracticeSessions);
        model.addAttribute("instrument", currentInstrument);

        return "records/select-session-records";
    }

    @RequestMapping(value="view", method = RequestMethod.POST)
    public String recordsView(Model model, int numberOfRecords, String instrumentId, HttpSession httpSession) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument currentInstrument = instrumentDao.findOne(instrId);
        User currentUser = currentInstrument.getUser();

        Iterable<Instrument> allUserInstruments = instrumentDao.findByUser_id(currentUser.getId());

        // sorts PracticeSessions for selected instrument by highest ID, which will list most recent PracticeSessions first
        List<PracticeSession> currentPracticeSessions = practiceSessionDao.findByInstrument_idOrderByDateDesc(currentInstrument.getId());

        // selects specified number of most recent PracticeSessions
        List<PracticeSession> selectedPracticeSessions = Helpers.limitPracticeSessionsBy(currentPracticeSessions, numberOfRecords);

        // makes list of unique (non-recurring) Skill objects across the selected PracticeSessions
        List<Skill> selectedSkills = Helpers.getAllSkillsPracticed(selectedPracticeSessions);

        // creates the skill rows to be used in the table display
        List<SkillDataRow> selectedSkillDataRows = Helpers.createSkillDataRows(selectedPracticeSessions);

        model.addAttribute("title", "Practice Table for " + currentInstrument.getName() + ": ");
        model.addAttribute("selectedPracticeSessions", selectedPracticeSessions);
        model.addAttribute("selectedSkills", selectedSkills);
        model.addAttribute("selectedSkillDataRows", selectedSkillDataRows);
        model.addAttribute("instrument", currentInstrument);

        return "records/view-session-records";

    }
}
