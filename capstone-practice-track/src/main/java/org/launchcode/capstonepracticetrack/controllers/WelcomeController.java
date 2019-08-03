package org.launchcode.capstonepracticetrack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("welcome")
public class WelcomeController {

    @RequestMapping(value = "")
    public String login(Model model) {

        model.addAttribute("title", "Welcome to PracticeTrack!");

        return "welcome/login";
    }


}
