package io.dwarf.ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private String href;
    private String uid;
    private Date expires;
    private String user_id;
    private int r_limit;
}
