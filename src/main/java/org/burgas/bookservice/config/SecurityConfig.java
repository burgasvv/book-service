package org.burgas.bookservice.config;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.entity.identity.Authority;
import org.burgas.bookservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(this.authenticationManager())
                .authorizeHttpRequests(
                        httpRequests -> httpRequests

                                .requestMatchers(
                                        "/api/v1/security/csrf-token",

                                        "/api/v1/identities/create",

                                        "/api/v1/documents/by-id",

                                        "/api/v1/authors", "/api/v1/authors/by-id",

                                        "/api/v1/publishers", "/api/v1/publishers/by-id",

                                        "/api/v1/books", "/api/v1/books/by-id"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/v1/identities/by-id", "/api/v1/identities/update",
                                        "/api/v1/identities/delete", "/api/v1/identities/change-password",
                                        "/api/v1/identities/add-book", "/api/v1/identities/remove-book"
                                )
                                .hasAnyAuthority(Authority.ADMIN.getAuthority(), Authority.USER.getAuthority())

                                .requestMatchers(
                                        "/api/v1/identities", "/api/v1/identities/change-status",

                                        "/api/v1/authors/create", "/api/v1/authors/update", "/api/v1/authors/delete",

                                        "/api/v1/publishers/create", "/api/v1/publishers/update", "/api/v1/publishers/delete",

                                        "/api/v1/books/create", "/api/v1/books/update", "/api/v1/books/delete",
                                        "/api/v1/books/upload-document", "/api/v1/books/delete-document"
                                )
                                .hasAnyAuthority(Authority.ADMIN.getAuthority())
                )
                .build();
    }
}
