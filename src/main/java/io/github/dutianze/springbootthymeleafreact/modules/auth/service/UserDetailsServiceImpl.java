package io.github.dutianze.springbootthymeleafreact.modules.auth.service;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import io.github.dutianze.springbootthymeleafreact.modules.account.repository.UserRepository;
import io.github.dutianze.springbootthymeleafreact.modules.auth.model.LoginUserDetail;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NullMarked
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameAndDeletedFalse(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("No user present with username: " + username));

    return new LoginUserDetail(user);
  }
}
