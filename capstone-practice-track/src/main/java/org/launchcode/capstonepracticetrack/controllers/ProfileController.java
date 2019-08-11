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

import java.util.List;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String viewProfilePage(Model model, @PathVariable int id) {

        User currentUser = userDao.findOne(id);

        List<Instrument> userInstruments = currentUser.getInstruments();

        model.addAttribute("appName", "PracticeTrack");
        model.addAttribute("title", "Welcome, " + currentUser.getUsername() + "!");
        model.addAttribute("instruments", userInstruments);

        return "profile/index";
    }
}
