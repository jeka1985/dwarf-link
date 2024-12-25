package io.dwarf.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@Controller

public class RootController {
    @GetMapping("/")
    public String messages(
            @CookieValue(value = "user_id") Optional<UUID> userIdCookie,
            Model model
    ) {
        if (userIdCookie.isPresent()) {
            model.addAttribute("userId", userIdCookie.get());
        }

        return "create";
    }
}