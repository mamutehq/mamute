package org.mamute.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListController {
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Mamute :: Home");
        return "home";
    }
}
