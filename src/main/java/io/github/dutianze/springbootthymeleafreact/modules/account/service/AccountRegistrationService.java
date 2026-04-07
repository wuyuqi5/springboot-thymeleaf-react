package io.github.dutianze.springbootthymeleafreact.modules.account.service;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.Role;
import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import io.github.dutianze.springbootthymeleafreact.modules.account.repository.RoleRepository;
import io.github.dutianze.springbootthymeleafreact.modules.account.repository.UserRepository;
import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.github.dutianze.springbootthymeleafreact.shared.util.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountRegistrationService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void register(String username, String rawPassword) {
    Preconditions.requireText("username", username);
    Preconditions.checkArgument(rawPassword != null && rawPassword.length() >= 4,
        "rawPassword", rawPassword);
    if (userRepository.existsByUsernameAndDeletedFalse(username)) {
      throw new BizException(ErrorCode.ACCOUNT_USERNAME_ALREADY_EXISTS);
    }

    String passwordHash = passwordEncoder.encode(rawPassword);
    if (passwordHash == null) {
      throw new BizException(ErrorCode.SYS_ERROR);
    }

    Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
        .orElseThrow(() -> new BizException(ErrorCode.ACCOUNT_REQUIRED_ROLE_MISSING));

    User account = new User(username, passwordHash);
    account.addRole(adminRole);
    userRepository.save(account);
  }
}
