package ch.zli.cospace.services;

import ch.zli.cospace.models.Role;
import ch.zli.cospace.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public boolean hasRole(Role role) {
        try {
            var auth = getAuthentication();
            auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).forEach(System.out::println);
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(role.name()));
        } catch (AuthenticationException e) {
            return false;
        }
    }

    public Optional<User> getUser() throws AuthenticationException {
       var auth = getAuthentication();
         return userService.findUserByEmail(auth.getName());
    }

    public boolean membertriesToChangeOtherUser(Long id) {
        try {
            var user = getUser().orElseThrow();
            if (user.getRole() == Role.MEMBER && !user.getId().equals(id)) {
                return true;
            }
        } catch (NoSuchElementException e) {
            return true;
        }
        return false;
    }

    private Authentication getAuthentication() throws AuthenticationException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User not authenticated") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

        return authentication;
    }
}

