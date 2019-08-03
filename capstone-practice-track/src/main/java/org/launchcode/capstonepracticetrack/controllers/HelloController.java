package org.launchcode.capstonepracticetrack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("Hello")
public class HelloController {


    @RequestMapping(value = "")
    @ResponseBody
    public String index() {
        return "Hello World!";
    }

    @RequestMapping(value = "goodbye")
    @ResponseBody
    public String goodbye() { return "Goodbye!"; }

}
