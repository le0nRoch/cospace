package ch.zli.cospace.controllers;

import ch.zli.cospace.models.Booking;
import ch.zli.cospace.models.Role;
import ch.zli.cospace.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "bookings", description = "Endpoints for booking management")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

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
}
