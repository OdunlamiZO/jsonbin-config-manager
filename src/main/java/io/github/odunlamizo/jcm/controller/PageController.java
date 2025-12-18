package io.github.odunlamizo.jcm.controller;

import io.github.odunlamizo.jcm.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ConfigService configService;

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
        model.addAttribute("projects", configService.getAllProjects());

        return "dashboard";
    }

    @GetMapping("/account")
    public String accountPage() {
        return "account";
    }
}
