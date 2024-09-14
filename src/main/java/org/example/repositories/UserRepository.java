package org.example.repositories;

import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsConfirmedFalse(String email);

    boolean existsByUsernameAndIsConfirmedTrue(String username);

    boolean existsByUsername(String email);
}
