package hexlet.code.app.service;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenAuthenticationService {

    private final JWTTokenService tokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public String login(final String username, final String password) {
        return userRepository.findByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> tokenService.expiring(Map.of("username", username)))
                .orElseThrow(() -> new UsernameNotFoundException("invalid login and/or password"));
    }


    public Optional<User> findByToken(final String token) {
        return userRepository.findByEmail(tokenService.verify(token).get("username").toString());
    }

}