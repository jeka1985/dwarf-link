package io.dwarf.api.users;

import io.dwarf.api.links.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface UsersRepo extends JpaRepository<User, String> {
    @Query(value = "SELECT l FROM Link l WHERE l.user_id = :id")
    List<Link> findAllLinks(@Param("id") UUID id);
}
