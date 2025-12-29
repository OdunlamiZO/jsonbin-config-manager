package io.github.odunlamizo.jcm.repository;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    @Query(
            "SELECT u FROM User u WHERE u.deletedAt IS NULL AND (:role IS NULL OR u.role = :role) AND (COALESCE(:term, '') = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<User> searchActive(
            @Param("term") String term, @Param("role") Role role, Pageable pageable);
}
