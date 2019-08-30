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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("welcome")
public class AuthenticationController extends AbstractBaseController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLogin(Model model, HttpSession session) {
        model.addAttribute("title", "Welcome to PracticeTrack!");

        if (session.getAttribute("user") == null) {
            model.addAttribute("nullMsg", "No user in session");

        }

        return "welcome/index";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, HttpSession session, @RequestParam String username, @RequestParam String password) {
        List<User> userList = userDao.findByUsername(username);

        if (userList.isEmpty()) {
            model.addAttribute("nameError", "User does not exist!");
            model.addAttribute("title", "Welcome to PracticeTrack!");
            return "welcome/index";
        }

        User currentUser =  userList.get(0);

        if (!currentUser.getPassword().equals(password)) {
            model.addAttribute("username", username);
            model.addAttribute("passwordError", "Incorrect password!");
            model.addAttribute("title", "Welcome to PracticeTrack!");
            return "welcome/index";
        }

        model.addAttribute("title", "Login Success! Welcome, " + username + "!");

        // put user in session
        session.setAttribute("user", currentUser.getId());

        return "redirect:/profile/" + currentUser.getId();

    }

    @RequestMapping(value="newAccount", method = RequestMethod.GET)
    public String viewNewAccountForm(Model model, @ModelAttribute User user ) {
        model.addAttribute("user", user);
        model.addAttribute("title", "Create an Account");
        model.addAttribute("error", " ");

        return "welcome/create-account";
    }

    @RequestMapping(value="newAccount", method = RequestMethod.POST)
    public String processNewAccountForm(Model model, @ModelAttribute @Valid User user, HttpSession session, Errors errors, String verify) {

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
            model.addAttribute("title", "Registration Success! Welcome, " + user.getUsername() + "!");

            userDao.save(user);

            // put user in session
            session.setAttribute("user", user.getId());

            return "redirect:/profile/" + user.getId();
        }
    }


}
