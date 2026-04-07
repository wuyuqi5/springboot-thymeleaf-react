package io.github.dutianze.springbootthymeleafreact.shared.config;

import io.github.dutianze.springbootthymeleafreact.modules.auth.filter.JwtAuthenticationFilter;
import io.github.dutianze.springbootthymeleafreact.modules.auth.handler.CustomAuthenticationFailureHandler;
import io.github.dutianze.springbootthymeleafreact.modules.auth.handler.CustomAuthenticationSuccessHandler;
import io.github.dutianze.springbootthymeleafreact.modules.auth.handler.JwtCookieClearingLogoutHandler;
import io.github.wimdeblauwe.htmx.spring.boot.security.HxLocationRedirectAccessDeniedHandler;
import io.github.wimdeblauwe.htmx.spring.boot.security.HxLocationRedirectAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtCookieClearingLogoutHandler jwtCookieClearingLogoutHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, "/login").permitAll()
            .requestMatchers("/register/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/assets/**").permitAll()
            .anyRequest().authenticated()
        )
        .securityContext(context -> context
            .securityContextRepository(new NullSecurityContextRepository())
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers
            .frameOptions(FrameOptionsConfig::sameOrigin)
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .failureHandler(customAuthenticationFailureHandler)
            .successHandler(customAuthenticationSuccessHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .addLogoutHandler(jwtCookieClearingLogoutHandler)
            .logoutSuccessUrl("/login"))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(handler -> handler
            .authenticationEntryPoint(new HxLocationRedirectAuthenticationEntryPoint("/login",
                hxRedirectStrategy(HttpStatus.UNAUTHORIZED)))
            .accessDeniedHandler(new HxLocationRedirectAccessDeniedHandler("/error",
                hxRedirectStrategy(HttpStatus.FORBIDDEN)))
        );
    return http.build();
  }

  private RedirectStrategy hxRedirectStrategy(HttpStatus status) {
    DefaultRedirectStrategy defaultStrategy = new DefaultRedirectStrategy();
    return (request, response, url) -> {
      if (request.getHeader("HX-Request") == null) {
        defaultStrategy.sendRedirect(request, response, url);
      } else {
        response.setHeader("HX-Location", request.getContextPath() + url);
        response.setStatus(status.value());
        response.getWriter().flush();
      }
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
