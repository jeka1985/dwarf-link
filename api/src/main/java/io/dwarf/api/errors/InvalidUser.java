package io.dwarf.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "No user with provided UUID")
public class InvalidUser extends RuntimeException {
}