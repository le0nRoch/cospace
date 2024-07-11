package ch.zli.cospace.services;

import ch.zli.cospace.dto.RegisterUserInput;
import ch.zli.cospace.models.Role;
import ch.zli.cospace.models.User;
import ch.zli.cospace.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User save(RegisterUserInput registerUserInput) {
        // If there are no users in the database, the first user will be an admin
        var role = Role.MEMBER;
        if (userRepository.count() == 0) {
            role = Role.ADMIN;
        }

        var user = User.builder()
                .email(registerUserInput.getEmail())
                .password(passwordEncoder.encode(registerUserInput.getPassword()))
                .firstName(registerUserInput.getFirstName())
                .lastName(registerUserInput.getLastName())
                .role(role)
                .build();
        userRepository.saveAndFlush(user);
        return user;
    }
}
