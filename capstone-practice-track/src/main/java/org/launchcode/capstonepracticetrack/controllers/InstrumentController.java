package org.launchcode.capstonepracticetrack.controllers;


import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

        return "profile/add-instrument";
    }
}
