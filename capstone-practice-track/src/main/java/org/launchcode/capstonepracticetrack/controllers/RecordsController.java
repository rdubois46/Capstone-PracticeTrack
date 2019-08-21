package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.SessionDao;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="records")
public class RecordsController {

    @Autowired
    InstrumentDao instrumentDao;

    @Autowired
    UserDao userDao;

    @Autowired
    SessionDao sessionDao;


    @RequestMapping(value="{InstrumentId}")
    public String recordsSelect(Model model, @PathVariable int InstrumentId, HttpSession httpSession) {

        Instrument currentInstrument = instrumentDao.findOne(InstrumentId);
        User currentUser = currentInstrument.getUser();
        int userId = currentUser.getId();

        Iterable<Instrument> allUserInstruments = instrumentDao.findByUser_id(userId);
        Iterable<Session> currentSessions =  sessionDao.findByInstrument_id(InstrumentId);

        httpSession.setAttribute("currentSessions", currentSessions);
        httpSession.setAttribute("currentUser", currentUser);
        httpSession.setAttribute("currentInstrument", currentInstrument);
        httpSession.setAttribute("allUserInstruments", allUserInstruments);


        model.addAttribute("title", "Select number of sessions to view for " + currentInstrument.getName() + ": ");
        model.addAttribute("userId", userId);
        model.addAttribute("currentSessions", currentSessions);

        return "records/view-session-records";
    }
}
