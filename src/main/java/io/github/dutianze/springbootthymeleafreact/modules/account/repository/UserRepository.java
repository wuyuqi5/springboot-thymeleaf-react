package io.github.dutianze.springbootthymeleafreact.modules.account.repository;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsernameAndDeletedFalse(String username);

  boolean existsByUsernameAndDeletedFalse(String username);

  boolean existsByUsernameIgnoreCaseAndDeletedFalse(String email);
}
