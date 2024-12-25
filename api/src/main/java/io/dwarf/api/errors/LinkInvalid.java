package io.dwarf.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Link is invalid")
public class LinkInvalid extends RuntimeException {
}