package io.github.odunlamizo.jcm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable throwable, Model model) {
        model.addAttribute(
                "errorMessage", throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }
}
