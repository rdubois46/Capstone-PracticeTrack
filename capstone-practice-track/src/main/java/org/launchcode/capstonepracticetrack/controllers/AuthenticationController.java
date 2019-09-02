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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("welcome")
public class AuthenticationController extends AbstractBaseController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLogin(Model model, Principal user, String error, HttpSession session, String logout) {

        if (user != null)
            return "redirect:/profile/";
        if (error != null)
            model.addAttribute("loginError", "Your username and/or password are invalid.");
        if (logout != null)
            model.addAttribute("logout", "You have successfully logged out.");

        model.addAttribute("title", "Welcome to PracticeTrack!");

        return "welcome/login";
    }

    @RequestMapping(value="newAccount", method = RequestMethod.GET)
    public String viewNewAccountForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("title", "Create an Account");

        return "welcome/create-account";
    }

    @RequestMapping(value="newAccount", method = RequestMethod.POST)
    public String processNewAccountForm(Model model, @ModelAttribute @Valid UserForm userForm, Errors errors,  HttpSession session, HttpServletRequest request) throws ServletException {

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
            model.addAttribute("title", "Create an Account");
            return "welcome/create-account";
        }

        User currentUser = userDao.findByUsername(userForm.getUsername());


        // put user in session

        session.setAttribute("user", currentUser);
        session.setAttribute("recentReg", true);

        // log in user upon successful registration
        authWithHttpServletRequest(request, currentUser.getUsername(), currentUser.getPassword());

        return "redirect:/profile/";

    }

    // method used after successful registration to automatically log in user
    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) throws ServletException {

        request.login(username, password);

    }

}
