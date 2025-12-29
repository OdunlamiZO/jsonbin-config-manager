package io.github.odunlamizo.jcm.controller;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.service.ConfigService;
import io.github.odunlamizo.jcm.service.UserService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public String userPage(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) String role) {
        Role roleEnum = null;
        if (role != null && !role.isBlank()) {
            try {
                roleEnum = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

        Page<User> userPage = userService.getUsers(page, size, search, roleEnum);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);

        // Role options (for forms)
        Map<String, String> roleOptions = new LinkedHashMap<>();
        roleOptions.put(Role.ADMIN.name(), "Admin");
        roleOptions.put(Role.EDITOR.name(), "Editor");
        roleOptions.put(Role.VIEWER.name(), "Viewer");
        model.addAttribute("roleOptions", roleOptions);

        // Filter options ('' maps to All Roles)
        Map<String, String> roleFilterOptions = new LinkedHashMap<>();
        roleFilterOptions.put("", "All Roles");
        roleFilterOptions.putAll(roleOptions);
        model.addAttribute("roleFilterOptions", roleFilterOptions);

        model.addAttribute("search", search);
        // Default role to '' (All Roles)
        model.addAttribute("role", role != null ? role : "");

        return "user";
    }
}
