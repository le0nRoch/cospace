package ch.zli.cospace.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User creator;

    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private String description;

    public Booking(User creator, LocalDate date, @NonNull TimeSlot timeSlot, @NonNull BookingStatus status, String description) {
        this.creator = creator;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.description = description;
    }
}
