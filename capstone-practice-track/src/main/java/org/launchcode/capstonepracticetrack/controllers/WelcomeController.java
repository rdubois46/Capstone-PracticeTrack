package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("welcome")
public class WelcomeController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String login(Model model) {
        model.addAttribute("title", "Welcome to PracticeTrack!");

        return "welcome/index";
    }

    @RequestMapping(value="NewAccount", method = RequestMethod.GET)
    public String viewNewAccountForm(Model model, @ModelAttribute User user ) {
        model.addAttribute("user", user);
        model.addAttribute("title", "Create an Account");
        model.addAttribute("error", " ");

        return "welcome/create-account";
    }

    @RequestMapping(value="NewAccount", method = RequestMethod.POST)
    public String processNewAccountForm(Model model, @ModelAttribute @Valid User user, Errors errors, String verify) {

        if (verify.isEmpty()) {
            model.addAttribute("error", "Must verify password!");
            user.setPassword("");
            model.addAttribute("user", user);
            model.addAttribute("title", "Create an Account");
            return "welcome/create-account";
        }

        if (!user.getPassword().equals(verify)) {
            model.addAttribute("error", "Passwords do not match!");
            user.setPassword("");
            model.addAttribute("user", user);
            model.addAttribute("title", "Create an Account");
            return "welcome/create-account";
        }

        if (errors.hasErrors()) {
            user.setPassword("");
            model.addAttribute("user", user);
            model.addAttribute("title", "Create an Account");
            return "welcome/create-account";
        } else {
            model.addAttribute("title", "Registration Success!");

            userDao.save(user);
            return "welcome/index";
        }
    }


}
