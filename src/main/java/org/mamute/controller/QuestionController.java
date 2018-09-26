package org.mamute.controller;

import org.mamute.util.TagsSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {
    private final TagsSplitter splitter;

    @Autowired
    public QuestionController(TagsSplitter splitter) {
        this.splitter = splitter;
    }

    @GetMapping("/ask")
    public String newQuestion() {
        return "ask";
    }
}
