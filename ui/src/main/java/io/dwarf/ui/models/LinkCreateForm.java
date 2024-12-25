package io.dwarf.ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LinkCreateForm {
    private String href;
    private int day_limits;
}
