package ch.zli.cospace.models;


import jakarta.persistence.*;
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
    private User creator;

    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;

    @NonNull
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
