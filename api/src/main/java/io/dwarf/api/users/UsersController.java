package io.dwarf.api.users;

import io.dwarf.api.links.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController

public class UsersController {
    private final UsersRepo repository;

    public UsersController(UsersRepo repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    User newUser() {
        return repository.save(new User());
    }

    @GetMapping("/users/{id}/links")
    List<Link> allUserLinks(
            @PathVariable UUID id
    ) {
        return repository.findAllLinks(id);
    }


}
