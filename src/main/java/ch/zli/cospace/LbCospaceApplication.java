package ch.zli.cospace;

import ch.zli.cospace.models.*;
import ch.zli.cospace.repositories.BookingRepository;
import ch.zli.cospace.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class LbCospaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbCospaceApplication.class, args);
    }

    @Profile("dev")
    @Bean
    public ApplicationRunner runner(UserRepository userRepository, BookingRepository bookingRepository) {
        return args -> {
            // TODO: add needed test data
            var user = new User(Role.ADMIN, "lro@gmx.ch", "gugus", "Leo", "Roch");
            var booking = new Booking(user, LocalDate.now(), TimeSlot.FULL_DAY, BookingStatus.REQUESTED, null);
            user.setBookings(List.of(booking));

            userRepository.saveAndFlush(user);
            bookingRepository.saveAndFlush(booking);
        };
    }
}
