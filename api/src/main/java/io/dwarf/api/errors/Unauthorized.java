package io.dwarf.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Permission denied")
public class Unauthorized extends RuntimeException {
}