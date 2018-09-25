package org.mamute.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {
    @GetMapping("/ask")
    public String newQuestion() {
        return "ask";
    }
}
