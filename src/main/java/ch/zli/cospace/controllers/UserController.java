package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.CreateUserInput;
import ch.zli.cospace.models.Role;
import ch.zli.cospace.models.User;
import ch.zli.cospace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "Endpoints for user management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/")
    @RolesAllowed(Role.Fields.ADMIN)
    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserInput userInput) {
        var user = User.builder()
                .email(userInput.getEmail())
                .password(userInput.getPassword())
                .firstName(userInput.getFirstName())
                .lastName(userInput.getLastName())
                .role(userInput.getRole())
                .build();
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
