package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.RegisterUserInput;
import ch.zli.cospace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthController {

    private final UserService userService;

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
}
