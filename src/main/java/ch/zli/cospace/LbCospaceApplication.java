package ch.zli.cospace;

import ch.zli.cospace.dto.RegisterUserInput;
import ch.zli.cospace.models.*;
import ch.zli.cospace.repositories.BookingRepository;
import ch.zli.cospace.services.UserService;
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
    public ApplicationRunner runner(UserService userService, BookingRepository bookingRepository) {
        return args -> {
            var registerUserInput = new RegisterUserInput("first.user.gets.admin@gmail.com", "admin123", "First", "AdminUser");
            var admin1Input = new RegisterUserInput("admin@gmail.com", "admin123", "Admin", "Admin");
            var toDeleteUser = new RegisterUserInput("deleteme@gmail.com", "deleteme", "Delete", "Me");
            var user2 = User.builder()
                    .email("user@gmail.com")
                    .password("useruser")
                    .firstName("User")
                    .lastName("User")
                    .role(Role.MEMBER)
                    .build();
            var booking = new Booking(user2, LocalDate.now(), TimeSlot.FULL_DAY, BookingStatus.REQUESTED, null);
            user2.setBookings(List.of(booking));

            userService.save(registerUserInput);
            userService.save(admin1Input);
            userService.save(toDeleteUser);
            userService.save(user2);
            bookingRepository.save(booking);

        };
    }
}
