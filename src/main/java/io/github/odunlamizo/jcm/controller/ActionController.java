package io.github.odunlamizo.jcm.controller;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.service.ConfigService;
import io.github.odunlamizo.jcm.service.UserService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ActionController {

    private final UserService userService;

    private final ConfigService configService;

    @PostMapping("/password/update")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(currentPassword, newPassword, confirmNewPassword);
            redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping("/project/create")
    public String createProject(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String collectionId,
            RedirectAttributes redirectAttributes) {
        try {
            configService.createProject(name, description, collectionId);
            redirectAttributes.addFlashAttribute("success", "Project created successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/project/{projectId}/env/{envId}/variable")
    public String addVariable(
            @PathVariable int projectId,
            @PathVariable int envId,
            @RequestParam String key,
            @RequestParam String value) {
        configService.addVariable(projectId, envId, key, value);

        return "redirect:/dashboard";
    }

    @PostMapping("/project/{projectId}/env/{envId}/variable/{key}/delete")
    public String deleteVariable(
            @PathVariable int projectId, @PathVariable int envId, @PathVariable String key) {
        configService.deleteVariable(projectId, envId, key);

        return "redirect:/dashboard";
    }

    @PostMapping("/user/add")
    public String addUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String role,
            RedirectAttributes redirectAttributes) {
        Role roleEnum = Role.VIEWER;
        if (Objects.nonNull(role) && !role.isBlank()) {
            try {
                roleEnum = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        try {
            userService.addUser(name, email, password, roleEnum);
            redirectAttributes.addFlashAttribute("success", "User added successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/user";
    }

    @PostMapping("/user/{userId}/delete")
    public String deleteUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/user";
    }
}
