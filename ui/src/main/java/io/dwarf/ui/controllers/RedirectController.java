package io.dwarf.ui.controllers;

import io.dwarf.ui.repositories.LinksRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class RedirectController {

    private final LinksRepository linksRepository;

    public RedirectController() {
        this.linksRepository = new LinksRepository();
    }

    @GetMapping("/r/{id}")
    public RedirectView r(@PathVariable String id) throws IOException, InterruptedException {
        return new RedirectView(this.linksRepository.getById(id).getHref());
    }
}