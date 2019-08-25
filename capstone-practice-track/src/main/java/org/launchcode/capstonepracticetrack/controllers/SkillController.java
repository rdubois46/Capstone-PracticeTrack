package org.launchcode.capstonepracticetrack.controllers;

import org.launchcode.capstonepracticetrack.Helpers;
import org.launchcode.capstonepracticetrack.models.Instrument;
import org.launchcode.capstonepracticetrack.models.Skill;
import org.launchcode.capstonepracticetrack.models.data.InstrumentDao;
import org.launchcode.capstonepracticetrack.models.data.SkillDao;
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
@RequestMapping("skill")
public class SkillController {

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private SkillDao skillDao;

    // unlike in user/instrument controllers, here the {id} is the *instrument* id, not the user id
    @RequestMapping(value = "add/{id}", method = RequestMethod.GET)
    public String viewAddSkillPage(Model model, @PathVariable int id) {

        Iterable<Skill> currentSkills = skillDao.findByInstrument_id(id);
        Instrument givenInstrument = instrumentDao.findById(id);

        // until I get session figured out, I have to keep passing userId back and forth from my views/controllers :(
        int userId = instrumentDao.findOne(id).getUser().getId();

        model.addAttribute("title", "Add a skill: ");
        model.addAttribute("skills", currentSkills);
        model.addAttribute("userId", userId);
        model.addAttribute("instrument", givenInstrument);
        model.addAttribute(new Skill());

        return "instrument/add-skill";
    }

    @RequestMapping(value = "add/{id}", method = RequestMethod.POST)
    public String processAddSkillForm(@ModelAttribute @Valid Skill skill, Errors errors, Model model, @PathVariable int id) {



        if (errors.hasErrors()) {
            Iterable<Skill> currentSkills = skillDao.findByInstrument_id(id);
            Instrument givenInstrument = instrumentDao.findById(id);

            // until I get session figured out, I have to keep passing userId back and forth from my views/controllers :(
            int userId = instrumentDao.findOne(id).getUser().getId();

            model.addAttribute("title", "Add a skill: ");
            model.addAttribute("skills", currentSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("instrument", givenInstrument);

            return "instrument/add-skill";
        }

        if (Helpers.doesSkillAlreadyExist(id, skill.getName(), skillDao)) {
            Iterable<Skill> currentSkills = skillDao.findByInstrument_id(id);
            Instrument givenInstrument = instrumentDao.findById(id);

            // until I get session figured out, I have to keep passing userId back and forth from my views/controllers :(
            int userId = instrumentDao.findOne(id).getUser().getId();

            model.addAttribute("title", "Add a skill: ");
            model.addAttribute("skills", currentSkills);
            model.addAttribute("userId", userId);
            model.addAttribute("instrument", givenInstrument);
            model.addAttribute("alreadyExistsError", "That skill is already in your skill List.");

            return "instrument/add-skill";

        }

        // makes the skill name lowercase in DB so we can easily check with Helpers.doesSkillAlreadyExist for future skill entries
        String lowercaseSkillName =  skill.getName().toLowerCase();
        skill.setName(lowercaseSkillName);

        Instrument givenInstrument = instrumentDao.findById(id);
        skill.setInstrument(givenInstrument);
        givenInstrument.addSkill(skill);
        skillDao.save(skill);

        return "redirect:/skill/add/{id}";
    }
}
