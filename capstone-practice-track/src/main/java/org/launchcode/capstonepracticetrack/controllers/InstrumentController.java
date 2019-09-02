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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("instrument")
public class InstrumentController extends AbstractBaseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private InstrumentDao instrumentDao;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String viewInstrumentAddPage(Model model, Principal principal) {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        Iterable<Instrument> currentInstruments = instrumentDao.findByUser_id(currentUser.getId());

        model.addAttribute("title", "Add an Instrument");
        model.addAttribute("instruments", currentInstruments);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute(new Instrument());

        return "instrument/add-instrument";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddInstrument(Model model, @ModelAttribute @Valid Instrument instrument, Errors errors, Principal principal) {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        if (errors.hasErrors()) {
            Iterable<Instrument> instruments = instrumentDao.findByUser_id(currentUser.getId());

            model.addAttribute("title", "Add an instrument");
            model.addAttribute("instruments", instruments);

            return "instrument/add-instrument";
        }

        // save new instrument
        instrument.setUser(currentUser);
        instrumentDao.save(instrument);

        // pull out updated list that contains the newly added instrument
        Iterable<Instrument> instruments = instrumentDao.findByUser_id(currentUser.getId());

        model.addAttribute("title", "Add an instrument");
        model.addAttribute("instruments", instruments);

        return "instrument/add-instrument";
    }
}
