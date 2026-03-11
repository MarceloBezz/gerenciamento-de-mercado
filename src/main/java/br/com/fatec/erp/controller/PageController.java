package br.com.fatec.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping({"/login", "/"})
    public String login(@RequestParam(required = false) String logout,
            @RequestParam(required = false) String error, Model model) {
        if (logout != null)
            model.addAttribute("logout", logout);
        if (error != null)
            model.addAttribute("error", error);
        return "login";
    }

}
