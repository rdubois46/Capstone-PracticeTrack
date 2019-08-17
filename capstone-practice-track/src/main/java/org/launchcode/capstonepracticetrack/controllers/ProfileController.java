package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.User;
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
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String viewProfilePage(Model model, HttpSession session, @PathVariable int id) {

        User currentUser = userDao.findOne(id);
        Object testId = session.getAttribute("user");

        Iterable<Instrument> instruments = currentUser.getInstruments();

        model.addAttribute("appName", "PracticeTrack");
        model.addAttribute("title", "Welcome, " + currentUser.getUsername() + "!");
        model.addAttribute("userId", id);
        model.addAttribute("instruments", instruments);
        model.addAttribute("testId", testId);

        return "profile/index";
    }

    @RequestMapping(value = "logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/welcome/login";
    }
}
