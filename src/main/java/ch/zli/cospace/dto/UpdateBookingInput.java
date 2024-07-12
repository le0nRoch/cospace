package ch.zli.cospace.dto;

import ch.zli.cospace.models.BookingStatus;
import ch.zli.cospace.models.TimeSlot;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingInput {
    private LocalDate date;
    private TimeSlot timeSlot;
    private BookingStatus status;
    private String description;
}
