package mk.ukim.finki.emagazine.service.impl;

import mk.ukim.finki.emagazine.model.User;
import mk.ukim.finki.emagazine.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.emagazine.repository.UserRepository;
import mk.ukim.finki.emagazine.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String username, String password) {
        return this.userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialsException::new);

    }
}
