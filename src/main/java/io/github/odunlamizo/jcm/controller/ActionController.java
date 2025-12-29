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
            @RequestParam(required = false) String collectionId) {
        configService.createProject(name, description, collectionId);

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
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String role) {
        Role roleEnum = Role.VIEWER;
        if (Objects.nonNull(role) && !role.isBlank()) {
            try {
                roleEnum = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        userService.addUser(name, email, password, roleEnum);

        return "redirect:/user";
    }

    @PostMapping("/user/{userId}/delete")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);

        return "redirect:/user";
    }

    @PostMapping("/user/{userId}/edit")
    public String editUser(
            @PathVariable int userId,
            @RequestParam String name,
            @RequestParam(required = false) String role) {
        Role roleEnum = null;
        if (Objects.nonNull(role) && !role.isBlank()) {
            try {
                roleEnum = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        userService.editUser(userId, name, roleEnum);

        return "redirect:/user";
    }
}
