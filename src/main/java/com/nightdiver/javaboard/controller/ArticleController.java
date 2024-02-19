package com.nightdiver.javaboard.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("articles", List.of());
        return "articles/index";
    }
}
