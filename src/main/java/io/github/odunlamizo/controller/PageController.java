package io.github.odunlamizo.controller;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Bad credentials");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("projects", Collections.emptyList());

        return "dashboard";
    }

    @GetMapping("/account")
    public String accountPage() {
        return "account";
    }
}
