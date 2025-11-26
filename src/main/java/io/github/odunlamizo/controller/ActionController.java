package io.github.odunlamizo.controller;

import io.github.odunlamizo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ActionController {

    private final UserService userService;

    @PostMapping("/password/update")
    public String updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updatePassword(currentPassword, newPassword, confirmNewPassword);
            redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/account";
    }
}
