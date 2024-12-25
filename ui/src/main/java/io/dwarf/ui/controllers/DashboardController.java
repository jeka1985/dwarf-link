package io.dwarf.ui.controllers;

import io.dwarf.ui.repositories.LinksRepository;
import io.dwarf.ui.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final UsersRepository usersRepository;
    private final LinksRepository linksRepository;

    public DashboardController() {
        this.linksRepository = new LinksRepository();
        this.usersRepository = new UsersRepository();
    }

    @RequestMapping("/{userId}")
    public String listLinks(
            @PathVariable UUID userId,
            Model model
    ) throws IOException, InterruptedException {
        model.addAttribute("links", this.usersRepository.getAllLinks(userId));

        return "dashboard/list";
    }

    @RequestMapping("/{userId}/link/{id}")
    public String editLink(
            @Value("${app.links.requests.min}") int minLimit,
            @Value("${app.links.requests.max}") int maxLimit,
            @CookieValue(value = "user_id") Optional<UUID> userIdCookie,
            @PathVariable UUID userId,
            @PathVariable String id,
            Model model
    ) throws IOException, InterruptedException {
        model.addAttribute("minLimit", minLimit);
        model.addAttribute("maxLimit", maxLimit);
        model.addAttribute("link", this.linksRepository.getByIdAsOwner(id, userId));
        model.addAttribute("userId", userIdCookie.get());

        return "dashboard/edit";
    }
}