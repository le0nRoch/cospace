package ch.zli.cospace.dto;


import ch.zli.cospace.models.TimeSlot;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateBookingInput {

    @NotNull
    private LocalDate date;

    @NotNull
    private TimeSlot timeSlot;
}
