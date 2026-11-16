/**
 * @author Trung Kien Huynh
 */
package sp.backend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sp.backend.exception.ExceptionMessage;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    // Passwörter verschlüsseln und vergleichen
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // SecurityContext enthält ein Authentication-Objekt mit Information über den angemeldeten User
    public SecurityContextRepository securityContextRepository() {
        // Den SecurityContext in der HTTP-Sitzung speichern
        // -> User bleibt über mehrere HTTP-Requests hinweg angemeldet
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // speichert CSRF-Token in Cookie "XSRF-TOKEN" und lädt den Token über "X-XSRF-TOKEN"-Request-Header
        // withHttpOnlyFalse ermöglicht JavaScript, auf das CSRF-Cookie zuzugreifen
        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        http
                // Cross-Origin Resource Sharing konfigurieren (durch corsConfigurationSource())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF-Schutz aktivieren und konfigurieren
                .csrf(csrf -> csrf
                        // Das zu verwendende CsrfTokenRepository festlegen
                        .csrfTokenRepository(csrfRepository)
                        // Den zu verwendenden CsrfTokenRequestHandler festlegen
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
                // Zugriffsberechtigungen für HTTP-Requests konfigurieren
                .authorizeHttpRequests(auth -> auth
                        // users
                        .requestMatchers("/api/users/me")
                        .authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*")
                        .authenticated()
                        // GET items
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/items",
                                "/api/items/*",
                                "/api/items/user/*"
                        )
                        .permitAll()
                        // Alle anderen items-Anfragen
                        .requestMatchers("/api/items/**")
                        .authenticated()
                        // chats
                        .requestMatchers("/api/chats/**")
                        .authenticated()
                        // messages
                        .requestMatchers("/api/messages/**")
                        .authenticated()
                        // favorites
                        .requestMatchers("/api/item-favorites/**")
                        .authenticated()
                        .anyRequest().permitAll())
                // Fehlerbehandlung konfigurieren
                .exceptionHandling(exception -> exception
                        // Nicht authentifizierter User versucht, auf eine geschützte Resource zuzugreifen
                        // -> mit "401 Unauthorized" antworten
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\":\"" + ExceptionMessage.USER_NOT_AUTHENTICATED + "\"}");
                        }))
                // Session-Management konfigurieren
                .sessionManagement(session ->
                        // erstellt eine HTTP-Session nur dann, wenn sie benötigt wird.
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // SecurityContext konfigurieren
                .securityContext(context ->
                        // Das zu verwendende SecurityContextRepository festlegen
                        context.securityContextRepository(securityContextRepository()))
                // Formularbasierte Authentifizierung von Spring Security aktivieren und konfigurieren
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        // HTTP-Parameter für Benutzername
                        .usernameParameter("accountName")
                        // HTTP-Parameter für Passwort
                        .passwordParameter("password")
                        // Anmeldung erfolgreich -> "200 OK"
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        // Anmeldung fehlgeschlagen -> "401 Unauthorized"
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\":\"Benutzername oder Passwort ist falsch.\"}");
                        })
                        // Erlaubt allen Users den Zugriff auf den Login-Endpunkt
                        .permitAll()
                )
                // Logout-Unterstützung von Spring Security aktivieren und konfigurieren
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true)
                        // Das Authentication-Objekt aus SecurityContext entfernen
                        .clearAuthentication(true)
                        // Das Session-Cookie "JSESSIONID" löschen.
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                )
                // HTTP-Basic-Authentifizierung von Spring Security aktivieren (für restTemplate.withBasicAuth() in Tests)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    // Cross-Origin Resource Sharing konfigurieren
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "PATCH")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        // erlaubt Cross-Origin-Requests mit Zugangsdaten
        // Requests aus Frontend enthalten credentials: "include" und werden daher blockiert bei setAllowCredentials(false)
        configuration.setAllowCredentials(true);

        // verwaltet, welche CORS-Regeln für welche URLs gelten.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // configuration gilt für "/**" (alle Pfade)
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
