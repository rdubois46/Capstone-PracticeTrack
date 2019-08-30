package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public abstract class AbstractBaseController {

    @Autowired
    UserService userService;

    protected static final String MESSAGE_KEY = "message";

    @ModelAttribute("user")
    public User getLoggedInUser(Principal principal) {
        if (principal != null)
            return userService.findByUsername(principal.getName());
        return null;
    }
}
