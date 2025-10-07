package oit.is.z9999.kaizi.janken.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Securityの認証設定（認証無効化）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authz -> authz
        .anyRequest().permitAll() // すべてのリクエストを許可
      )
      .csrf().disable() // CSRF保護を無効化（必要に応じて）
      .formLogin().disable() // Spring Securityのフォームログインを無効化
      .logout().disable(); // ログアウトも無効化
    return http.build();
  }
}
