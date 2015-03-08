package org.appbricks.service.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class HomePageController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String greeting(@RequestParam(value="name", defaultValue="World") String name, Model model) {

        // let’s pass some variables to the view script
        model.addAttribute("id", counter.incrementAndGet());
        model.addAttribute("content", String.format(template, name));

        // renders /templates/hello.jsp
        return "home";
    }
}