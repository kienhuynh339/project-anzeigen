/**
 * @author Trung Kien Huynh
 */
package sp.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.User;
import sp.backend.user.User;
import org.springframework.stereotype.Service;
import sp.backend.user.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    // wird beim Login von Spring Security aufgerufen, um User aus der Datenbank zu laden
    public UserDetails loadUserByUsername(String accountName) {

        User user = userRepository.findByAccountName(accountName).orElseThrow(() ->
                // wird in formLogin().failureHandler() abgefangen.
                new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getAccountName())
                .password(user.getPasswordHash()) // Passwort-Hashes werden automatisch verglichen.
                .authorities(Collections.emptyList())
                .build();
    }
}