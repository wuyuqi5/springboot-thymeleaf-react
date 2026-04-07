package io.github.dutianze.springbootthymeleafreact.modules.auth.service;

import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.CLAIM_TOKEN_TYPE;
import static io.github.dutianze.springbootthymeleafreact.modules.auth.constant.JwtConstants.TOKEN_TYPE_REFRESH;

import io.github.dutianze.springbootthymeleafreact.modules.account.modal.Role;
import io.github.dutianze.springbootthymeleafreact.modules.account.modal.User;
import io.github.dutianze.springbootthymeleafreact.modules.auth.model.LoginUserDetail;
import io.github.dutianze.springbootthymeleafreact.shared.config.JwtProperties;
import io.github.dutianze.springbootthymeleafreact.shared.exception.BizException;
import io.github.dutianze.springbootthymeleafreact.shared.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperties jwtProperties;

  public String generateAccessToken(User user) {
    Instant now = Instant.now();
    Instant expiration = now.plus(jwtProperties.accessTokenExpiration());
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("ver", jwtProperties.tokenVersion())
        .claim("userId", user.getId())
        .claim("roles", user.getRoles().stream().map(Role::getRoleName).sorted().toList())
        .claim("isActive", user.isActive())
        .claim("name", user.getName())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(signingKey())
        .compact();
  }

  public String generateRefreshToken(User user) {
    Instant now = Instant.now();
    Instant expiration = now.plus(jwtProperties.refreshTokenExpiration());
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("ver", jwtProperties.tokenVersion())
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(signingKey())
        .compact();
  }

  @SuppressWarnings("unchecked")
  public LoginUserDetail buildUserDetail(String token) {
    Claims claims = parseClaims(token);

    if (TOKEN_TYPE_REFRESH.equals(claims.get(CLAIM_TOKEN_TYPE, String.class))) {
      throw new BizException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_ALLOWED);
    }

    List<String> roleNames = claims.get("roles", List.class);
    Set<Role> roles = new LinkedHashSet<>();
    if (roleNames != null) {
      for (String roleName : roleNames) {
        if (StringUtils.isBlank(roleName)) {
          continue;
        }
        roles.add(new Role(roleName));
      }
    }

    User user = new User(
        claims.get("userId", Long.class),
        claims.getSubject(),
        claims.get("name", String.class),
        Boolean.TRUE.equals(claims.get("isActive", Boolean.class)),
        roles
    );
    return new LoginUserDetail(user);
  }

  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean isRefreshToken(String token) {
    try {
      return TOKEN_TYPE_REFRESH.equals(parseClaims(token).get(CLAIM_TOKEN_TYPE, String.class));
    } catch (JwtException e) {
      return false;
    }
  }

  public boolean isTokenVersionValid(String token) {
    try {
      Integer ver = parseClaims(token).get("ver", Integer.class);
      return Integer.valueOf(jwtProperties.tokenVersion()).equals(ver);
    } catch (JwtException e) {
      return false;
    }
  }

  public boolean isTokenValid(String token) {
    if (StringUtils.isEmpty(token)) {
      return false;
    }
    try {
      parseClaims(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(signingKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey signingKey() {
    return Keys.hmacShaKeyFor(jwtProperties.jwtSecret().getBytes(StandardCharsets.UTF_8));
  }
}
