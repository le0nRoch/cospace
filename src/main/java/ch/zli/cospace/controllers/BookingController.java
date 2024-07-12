package ch.zli.cospace.controllers;

import ch.zli.cospace.dto.CreateBookingInput;
import ch.zli.cospace.dto.UpdateBookingInput;
import ch.zli.cospace.models.Booking;
import ch.zli.cospace.models.BookingStatus;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new booking", responses = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> createBooking(@Valid @RequestBody CreateBookingInput bookingInput) {

        try {
            var user = authService.getUser().orElseThrow();
            var booking = Booking.builder()
                    .creator(user)
                    .timeSlot(bookingInput.getTimeSlot())
                    .status(BookingStatus.REQUESTED)
                    .date(bookingInput.getDate())
                    .build();

            bookingService.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // member should be able to cancel his own booking
    // admin should be able to edit all bookings
    @PatchMapping("/{id}")
    @Operation(summary = "Update a booking", responses = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<Void> updateBooking(@PathVariable Long id, @RequestBody UpdateBookingInput bookingInput) {

        try {
            var user = authService.getUser().orElseThrow();

            if (user.getRole() == Role.MEMBER) {

                // member should not be able to edit other members bookings
                if (user.getBookings().stream().noneMatch(booking -> booking.getId().equals(id))) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                // member can only cancel his own booking
                if (bookingInput.getStatus() != BookingStatus.CANCELLED) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                try {
                    var booking = bookingService.findById(id).orElseThrow();
                    booking.setStatus(BookingStatus.CANCELLED);
                    bookingService.update(booking);
                    return ResponseEntity.ok().build();
                } catch (NoSuchElementException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            }

            // admin can edit all bookings
            try {
                var booking = bookingService.findById(id).orElseThrow();
                if (bookingInput.getDate() != null) {
                    booking.setDate(bookingInput.getDate());
                }
                if (bookingInput.getTimeSlot() != null) {
                    booking.setTimeSlot(bookingInput.getTimeSlot());
                }
                if (bookingInput.getStatus() != null) {
                    booking.setStatus(bookingInput.getStatus());
                }
                if (bookingInput.getDescription() != null) {
                    booking.setDescription(bookingInput.getDescription());
                }
                bookingService.update(booking);
                return ResponseEntity.ok().build();
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
