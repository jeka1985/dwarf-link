package io.dwarf.api.links;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LinksRepo extends JpaRepository<Link, String> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Link l where l.expires < current_timestamp OR l.r_limit = 0")
    void cleanup();
}
