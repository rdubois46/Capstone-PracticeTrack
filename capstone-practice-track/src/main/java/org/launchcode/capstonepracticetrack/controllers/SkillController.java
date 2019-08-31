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
public class SkillController extends AbstractBaseController {

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private SkillDao skillDao;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String viewAddSkillPage(Model model, String instrumentId) {

        int instrId = Integer.parseInt(instrumentId);

        Instrument givenInstrument = instrumentDao.findById(instrId);
        Iterable<Skill> currentSkills = skillDao.findByInstrument_id(instrId);


        model.addAttribute("title", "Add a skill: ");
        model.addAttribute("instrument", givenInstrument);
        model.addAttribute("skills", currentSkills);

        model.addAttribute(new Skill());

        return "instrument/add-skill";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddSkillForm(@ModelAttribute @Valid Skill skill, Errors errors, Model model, String instrumentId) {

        int instrId = Integer.parseInt(instrumentId);
        Instrument givenInstrument = instrumentDao.findById(instrId);

        if (errors.hasErrors()) {

            Iterable<Skill> currentSkills = skillDao.findByInstrument_id(instrId);

            model.addAttribute("title", "Add a skill: ");
            model.addAttribute("skills", currentSkills);
            model.addAttribute("instrument", givenInstrument);

            return "instrument/add-skill";
        }

        if (Helpers.doesSkillAlreadyExist(instrId, skill.getName(), skillDao)) {

            Iterable<Skill> currentSkills = skillDao.findByInstrument_id(instrId);

            model.addAttribute("title", "Add a skill: ");
            model.addAttribute("skills", currentSkills);
            model.addAttribute("instrument", givenInstrument);
            model.addAttribute("alreadyExistsError", "That skill is already in your skill List.");

            return "instrument/add-skill";

        }

        // makes the skill name lowercase in DB so we can easily check with Helpers.doesSkillAlreadyExist for future skill entries
        String lowercaseSkillName =  skill.getName().toLowerCase();
        skill.setName(lowercaseSkillName);

        // put new skill in DB
        skill.setInstrument(givenInstrument);
        givenInstrument.addSkill(skill);
        skillDao.save(skill);

        // get updated list of skills
        Iterable<Skill> currentSkills = skillDao.findByInstrument_id(instrId);

        model.addAttribute("title", "Add a skill: ");
        model.addAttribute("skills", currentSkills);
        model.addAttribute("instrument", givenInstrument);

        return "instrument/add-skill";
    }
}
