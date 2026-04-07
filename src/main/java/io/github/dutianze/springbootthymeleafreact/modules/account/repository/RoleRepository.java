package io.github.dutianze.springbootthymeleafreact.modules.account.repository;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByRoleName(String roleName);
}
