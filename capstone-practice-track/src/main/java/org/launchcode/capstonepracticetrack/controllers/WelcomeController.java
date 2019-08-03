package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("welcome")
public class WelcomeController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String login(Model model) {

        model.addAttribute("title", "Welcome to PracticeTrack!");

        return "welcome/login";
    }


}
