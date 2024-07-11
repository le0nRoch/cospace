package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.LoginUserInput;
import ch.zli.cospace.dto.RegisterUserInput;
import ch.zli.cospace.models.User;
import ch.zli.cospace.security.JwtUtils;
import ch.zli.cospace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthController {

    private final UserService userService;
    private final DaoAuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserInput authRequest) {
        userService.save(authRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", responses = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> login(@Valid @RequestBody LoginUserInput loginInput) {
        try {
            var authentication = authenticationProvider
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginInput.getEmail(), loginInput.getPassword()
                            )
                    );

            var user = (User) authentication.getPrincipal();

            var jwt = jwtUtils.generateToken(user.getId(), user.getEmail());

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .build();
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

    }
}
