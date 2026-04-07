package io.github.dutianze.springbootthymeleafreact.modules.account.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(
    name = "app_user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
    }
)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "app_user_role",
      joinColumns = @JoinColumn(
          name = "user_id",
          nullable = false,
          foreignKey = @ForeignKey(name = "fk_app_user_role_user")
      ),
      inverseJoinColumns = @JoinColumn(
          name = "role_id",
          nullable = false,
          foreignKey = @ForeignKey(name = "fk_app_user_role_role")
      )
  )
  private Set<Role> roles = new LinkedHashSet<>();

  @Column(length = 255)
  private String username;

  @Column(length = 255)
  private String name;

  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public User(String username, String passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
  }

  public User(Long id, String username, String name, boolean isActive, Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.isActive = isActive;
    this.roles = new LinkedHashSet<>(roles);
  }

  public void addRole(Role role) {
    roles.add(role);
    role.getUsers().add(this);
  }
}
