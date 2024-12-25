package io.dwarf.api.links;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    private String href;

    @Column(updatable = false)
    @Timestamp
    private Date expires;

    @Column(updatable = false)
    private UUID user_id;

    @Column(insertable = false)
    private int r_limit;
}
