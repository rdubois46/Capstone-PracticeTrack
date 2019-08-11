package org.launchcode.capstonepracticetrack.controllers;


import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.User;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("instrument")
public class InstrumentController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private InstrumentDao instrumentDao;

    @RequestMapping(value="add/{id}", method = RequestMethod.GET)
    public String viewInstrumentAddPage(Model model, @PathVariable int id) {

        Iterable<Instrument> instruments = instrumentDao.findByUser_id(id);

        model.addAttribute("title", "Add an Instrument");
        model.addAttribute("instruments", instruments);
        model.addAttribute("userId", id);
        model.addAttribute(new Instrument());

        return "profile/add-instrument";
    }

    @RequestMapping(value="add/{id}", method = RequestMethod.POST)
    public String processAddInstrument(Model model, @ModelAttribute @Valid Instrument instrument, Errors errors, @PathVariable int id) {


        if (errors.hasErrors()) {
            Iterable<Instrument> instruments = instrumentDao.findByUser_id(id);

            model.addAttribute("title", "Add an instrument");
            model.addAttribute("instruments", instruments);
            model.addAttribute("userId", id);

            return "profile/add-instrument";
        }

        User currentUser = userDao.findOne(id);
        instrument.setUser(currentUser);
        currentUser.addInstrument(instrument);
        instrumentDao.save(instrument);

        return "redirect:/instrument/add/" + id;
    }
}
