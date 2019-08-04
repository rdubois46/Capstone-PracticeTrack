package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("profile")
public class ProfileController {

    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewProfilePage(Model model) {
        model.addAttribute("title", "Your profile");
        return "profile/index";
    }
}
