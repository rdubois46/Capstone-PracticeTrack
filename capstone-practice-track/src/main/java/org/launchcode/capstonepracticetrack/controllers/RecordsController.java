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


    @RequestMapping(value="{id}")
    public String recordsView(Model model, @PathVariable int id, HttpSession httpSession) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        User currentUser = currentInstrument.getUser();
        int userId = currentUser.getId();

        List<Instrument> allUserInstruments = instrumentDao.findByUser_id(userId);
        List<Session> currentSessions = sessionDao.findByUser_id(userId);

        httpSession.setAttribute("currentSessions", currentSessions);
        httpSession.setAttribute("currentUser", currentUser);
        httpSession.setAttribute("currentInstrument", currentInstrument);
        httpSession.setAttribute("allUserInstruments", allUserInstruments);


        model.addAttribute("title", "Select number of sessions to view for " + currentInstrument.getName() + ": ");
        model.addAttribute("userId", userId);

        return "records/view-session-records";
    }
}
