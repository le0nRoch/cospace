package ch.zli.cospace.controllers;

import ch.zli.cospace.models.Booking;
import ch.zli.cospace.models.Role;
import ch.zli.cospace.services.AuthService;
import ch.zli.cospace.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/bookings")
@Tag(name = "bookings", description = "Endpoints for booking management")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final AuthService authService;

    @GetMapping("/")
    @RolesAllowed(Role.Fields.ADMIN)
    @Operation(summary = "Get all bookings", responses = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
    })
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get all bookings of a user", responses = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
    })
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable(name = "userId") Long userId) {

        if (authService.membertriesToChangeOtherUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

        return ResponseEntity.ok(bookingService.findByUserId(userId));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
