package io.github.odunlamizo.jcm.controller;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.service.ConfigService;
import io.github.odunlamizo.jcm.service.UserService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;

    private final ConfigService configService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error, Model model) {
        if (Objects.nonNull(error)) {
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

    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        // Provide role options for the custom select (enum name -> label)
        Map<String, String> roleOptions = new LinkedHashMap<>();
        roleOptions.put(Role.ADMIN.name(), "Admin");
        roleOptions.put(Role.EDITOR.name(), "Editor");
        roleOptions.put(Role.VIEWER.name(), "Viewer");
        model.addAttribute("roleOptions", roleOptions);

        return "user";
    }
}
