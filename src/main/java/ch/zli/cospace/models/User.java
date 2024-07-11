package ch.zli.cospace.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "creator")
    private List<Booking> bookings;

    public User(Role role, String email, String firstName, String lastName, String password) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}