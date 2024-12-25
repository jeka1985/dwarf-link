package io.dwarf.api.links;

import io.dwarf.api.errors.InvalidUser;
import io.dwarf.api.errors.LinkGone;
import io.dwarf.api.errors.LinkInvalid;
import io.dwarf.api.errors.Unauthorized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class LinksController {
    private final LinksRepo repository;

    public LinksController(LinksRepo repository) {
        this.repository = repository;
    }

    private static boolean checkIsValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            throw new LinkInvalid();
        }
    }

    private static int getSafeRequestLimit(int limit, int min, int max) {
        if (limit < min) {
            return min;
        }

        return Math.min(limit, max);
    }

    private boolean checkIfOwner(UUID userId, UUID linkUserId) {
        if (!userId.equals(linkUserId)) throw new Unauthorized();

        return true;
    }

    private boolean checkIfRegistered(UUID userUUID) {
        if (userUUID == null) throw new InvalidUser();

        return true;
    }

    private boolean checkIfLimitsAvailable(int limit) {
        if (limit <= 0) throw new LinkGone();

        return true;
    }


    @GetMapping("/links")
    List<Link> all() {
        return repository.findAll();
    }

    @GetMapping("/links/gc")
    List<Link> cleanuoLinks() {
        return repository.findAll();
    }

    @PostMapping("/links")
    Link newLink(
            @Value("${app.links.requests.max}") int maxLimit,
            @Value("${app.links.requests.min}") int minLimit,
            @RequestHeader("X-User-UUID") UUID userUUID,
            @RequestBody Link link
    ) {
        this.checkIfRegistered(userUUID);
        checkIsValidURL(link.getHref());


        link.setUser_id(userUUID);
        link.setR_limit(getSafeRequestLimit(link.getR_limit(), minLimit, maxLimit));

        return repository.save(link);
    }

    @PutMapping("/links/{id}")
    Link updateLink(
            @Value("${app.links.requests.max}") int maxLimit,
            @Value("${app.links.requests.min}") int minLimit,
            @RequestHeader("X-User-UUID") UUID userUUID,
            @RequestBody Link nextLink,
            @PathVariable String id
    ) {
        this.checkIfRegistered(userUUID);
        checkIsValidURL(nextLink.getHref());

        Optional<Link> link = repository.findById(id);

        link.ifPresent(value -> this.checkIfOwner(userUUID, value.getUser_id()));

        return link
                .map(address -> {
                    address.setHref(nextLink.getHref());
                    address.setR_limit(getSafeRequestLimit(nextLink.getR_limit(), minLimit, maxLimit));
                    return repository.save(address);
                })
                .orElseGet(() -> repository.save(nextLink));
    }

    @GetMapping("/links/{id}")
    Optional<Link> getLink(
            @PathVariable String id,
            @RequestHeader("X-User-UUID") Optional<UUID> userUUID
    ) {
        Optional<Link> item = repository.findById(id);

        if (userUUID.isEmpty() && item.isPresent()) {
            Link link = item.get();
            long expires = link.getExpires().getTime();
            int rLimit = link.getR_limit();

            boolean hasLimits = this.checkIfLimitsAvailable(rLimit);

            if (!hasLimits || System.currentTimeMillis() > expires) {
                repository.delete(link);

                throw new LinkGone();
            }

            link.setR_limit(rLimit - 1);

            repository.save(link);
        }

        return item;
    }

    @DeleteMapping("/links/{id}")
    void deleteLink(
            @RequestHeader("X-User-UUID") UUID userUUID,
            @PathVariable String id
    ) {
        Optional<Link> item = repository.findById(id);

        if (item.isPresent()) {
            Link link = item.get();

            checkIfOwner(userUUID, item.get().getUser_id());

            repository.delete(link);
            return;
        }

        throw new LinkGone();
    }
}
