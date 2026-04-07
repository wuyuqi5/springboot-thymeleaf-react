package io.github.dutianze.springbootthymeleafreact.modules.auth.model;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.Role;
import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import java.io.Serial;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record LoginUserDetail(User user) implements UserDetails, CredentialsContainer {

  @Serial
  private static final long serialVersionUID = 7694270747586948579L;

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getPasswordHash();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(Role::getRoleName)
        .filter(roleName -> roleName != null && !roleName.isBlank())
        .sorted(Comparator.naturalOrder())
        .map(SimpleGrantedAuthority::new)
        .toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isActive();
  }

  @Override
  public void eraseCredentials() {
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof LoginUserDetail other) {
      return Objects.equals(user.getId(), other.user().getId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(user.getId());
  }
}
