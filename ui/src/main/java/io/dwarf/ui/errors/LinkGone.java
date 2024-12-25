package io.dwarf.ui.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE, reason = "Link no longer available")
public class LinkGone extends RuntimeException {
}