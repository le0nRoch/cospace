package ch.zli.cospace.security;

import ch.zli.cospace.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // validate authorizazion header
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // validate jwt token
        var token = header.split(" ")[1].trim();
        if (jwtUtils.isInvalid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // get user identity and set it on the spring security context
        var optionalUser = userService.findUserByEmail(jwtUtils.getEmail(token));

        if (optionalUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                optionalUser.get(), null, optionalUser.get().getAuthorities()
        );

        // trust me on this😉
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
