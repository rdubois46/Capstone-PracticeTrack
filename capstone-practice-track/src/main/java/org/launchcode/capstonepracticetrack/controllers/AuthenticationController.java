package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.forms.UserForm;
import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.launchcode.capstonepracticetrack.user.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("welcome")
public class AuthenticationController extends AbstractBaseController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLogin(Model model, Principal user, String error, HttpSession session, @RequestParam(required=false) boolean logout) {
        model.addAttribute("title", "Welcome to PracticeTrack!");

        if (user != null)
            return "redirect:/profile/";

        if (error != null)
            model.addAttribute(MESSAGE_KEY, "danger|Your username and/or password are invalid.");

        if (logout)
            model.addAttribute(MESSAGE_KEY, "info|You have successfully logged out.");

        if (session.getAttribute("user") == null) {
            model.addAttribute("nullMsg", "No user in session");

        }

        return "welcome/login";
    }

    /*@RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        User currentUser = userDao.findByUsername(username);

        if (currentUser == null) {
            model.addAttribute("nameError", "User does not exist!");
            model.addAttribute("title", "Welcome to PracticeTrack!");
            return "welcome/login";
        }

        if (!currentUser.getPassword().equals(password)) {
            model.addAttribute("username", username);
            model.addAttribute("passwordError", "Incorrect password!");
            model.addAttribute("title", "Welcome to PracticeTrack!");
            return "welcome/login";
        }

        model.addAttribute("title", "Login Success! Welcome, " + username + "!");

        // put user in session
        session.setAttribute("user", currentUser.getId());

        return "redirect:/profile/" + currentUser.getId();

    }*/

    @RequestMapping(value="newAccount", method = RequestMethod.GET)
    public String viewNewAccountForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("title", "Create an Account");

        return "welcome/create-account";
    }

    @RequestMapping(value="newAccount", method = RequestMethod.POST)
    public String processNewAccountForm(Model model, @ModelAttribute @Valid UserForm userForm, Errors errors,  HttpSession session) {

        if (errors.hasErrors()) {
            userForm.setPassword("");
            userForm.setVerifyPassword("");
            model.addAttribute("userForm", userForm);
            model.addAttribute("title", "Create an Account");
            return "welcome/create-account";
        }

        try {
            userService.save(userForm);
        } catch (UsernameExistsException e) {
            errors.rejectValue("username", "username.alreadyexists", e.getMessage());
        }

        User currentUser = userDao.findByUsername(userForm.getUsername());
        model.addAttribute("title", "Registration Success! Welcome, " + currentUser.getUsername() + "!");

        // put user in session
        session.setAttribute("user", currentUser);

        return "redirect:/profile/" + currentUser.getId();

    }

}
