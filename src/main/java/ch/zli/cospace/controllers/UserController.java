package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.CreateUserInput;
import ch.zli.cospace.dto.UpdateUserInput;
import ch.zli.cospace.models.Role;
import ch.zli.cospace.models.User;
import ch.zli.cospace.services.AuthService;
import ch.zli.cospace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "Endpoints for user management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;


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

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserInput userInput) {

        // Check if user is authorized to update user
        try {
            var user = authService.getUser().orElseThrow();
            if (user.getRole() == Role.MEMBER && !user.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // check if user is trying to update role and is not an admin
        if (authService.hasRole(Role.MEMBER) && userInput.getRole() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            var user = userService.findUserById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

            if (userInput.getEmail() != null) {
                user.setEmail(userInput.getEmail());
            }

            if (userInput.getPassword() != null) {
                user.setPassword(userInput.getPassword());
            }

            if (userInput.getFirstName() != null) {
                user.setFirstName(userInput.getFirstName());
            }

            if (userInput.getLastName() != null) {
                user.setLastName(userInput.getLastName());
            }

            if (userInput.getRole() != null) {
                user.setRole(userInput.getRole());
            }

            userService.save(user);

            return ResponseEntity.ok().build();

        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
