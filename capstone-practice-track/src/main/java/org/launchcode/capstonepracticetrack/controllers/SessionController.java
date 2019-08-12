package org.launchcode.capstonepracticetrack.controllers;


import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.Session;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("session")
public class SessionController {

    private InstrumentDao instrumentDao;

    private SkillDao skillDao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String createSession(Model model, @PathVariable int id) {

        Instrument currentInstrument = instrumentDao.findOne(id);
        Iterable<Skill> givenSkills = currentInstrument.getSkills();

        model.addAttribute("title", "Create new practice session for the " + currentInstrument.getName());
        model.addAttribute("instrument", currentInstrument);
        model.addAttribute("skills", givenSkills);
        model.addAttribute(new Session());

        return "session/add-session";
    }

}
