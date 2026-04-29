package io.github.pluton33.ezgloszenie.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { //zwraca funkcje hashującą hasło
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable()) //TYLKO DO TESTOWANIA
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/v3/api-docs/**",    //żeby swagger działał w przeglądarce
//                                "/swagger-ui/**",
//                                "/swagger-ui.html"
//                        )
                        .anyRequest()
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin((form) -> form
                        // po sukcesie wysyłaj OK
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // Wyślij 200 OK zamiast 302
                        })

                        // po błędzie wysyłaj UNAUTHORIZED
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Wyślij 401 zamiast 302
                        })
                        .permitAll()
                )
//                .formLogin(withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }


}
