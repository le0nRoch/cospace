package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.RegisterUserInput;
import ch.zli.cospace.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RestControllerAdvice
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException e) {
        // TODO: Convert the constraint violations to a JSON response
        var message = e.getConstraintViolations().stream()
                .map(v -> String.format("\"%s\" : \"%s\",", v.getPropertyPath(), v.getMessage()))
                .reduce("", (a, b) -> String.format("%s,\n%s", a, b));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(String.format("{ \"error\": { %s} }", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        // TODO: Convert the constraint violations to a JSON response
        var message = e.getBindingResult().getFieldErrors().stream()
                .map(v -> String.format("\"%s\" : \"%s\",", v.getField(), v.getDefaultMessage()))
                .reduce("", (a, b) -> String.format("%s,\n%s", a, b));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(String.format("{ \"error\": { %s} }", message));
    }
}
