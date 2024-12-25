package io.dwarf.ui.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dwarf.ui.models.Link;
import io.dwarf.ui.models.LinkCreateForm;
import io.dwarf.ui.models.LinkEditForm;
import io.dwarf.ui.models.User;
import io.dwarf.ui.repositories.LinksRepository;
import io.dwarf.ui.repositories.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/bff")
public class BFFController {
    private final UsersRepository usersRepository;
    private final LinksRepository linksRepository;

    public BFFController() {
        this.usersRepository = new UsersRepository();
        this.linksRepository = new LinksRepository();
    }

    @RequestMapping(value = "/links/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectNode deleteLink(
            @CookieValue(value = "user_id") Optional<UUID> userIdCookie,
            @PathVariable String id
    ) throws IOException, InterruptedException {
        ObjectMapper requestMapper = new ObjectMapper();
        ObjectNode payload = requestMapper.createObjectNode();

        payload.put("id", id);
        this.linksRepository.delete(id, userIdCookie.get());

        return payload;
    }

    @RequestMapping(value = "/links/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Link updateLink(
            @CookieValue(value = "user_id") Optional<UUID> userIdCookie,
            @PathVariable String id,
            @RequestBody LinkEditForm form

    ) throws IOException, InterruptedException {
        return this.linksRepository.update(id, form.getHref(), form.getR_limit(), userIdCookie.get());
    }

    @RequestMapping(value = "/links/", method = RequestMethod.POST)
    @ResponseBody
    public Link createLink(
            @CookieValue(value = "user_id") Optional<UUID> userIdCookie,
            @RequestBody LinkCreateForm form,
            HttpServletResponse response
    ) throws IOException, InterruptedException {
        String href = form.getHref();
        int daysDuration = form.getDay_limits();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, daysDuration);
        String expires = String.valueOf(c.getTimeInMillis());

        if (userIdCookie.isPresent()) {
            return this.linksRepository.create(href, expires, userIdCookie.get());
        } else {
            User user = this.usersRepository.create();
            Cookie cookie = new Cookie("user_id", user.getId().toString());

            cookie.setPath("/");
            response.addCookie(cookie);

            return this.linksRepository.create(href, expires, user.getId());
        }
    }
}