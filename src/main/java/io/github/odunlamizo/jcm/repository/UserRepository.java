package io.github.odunlamizo.jcm.repository;

import io.github.odunlamizo.jcm.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
