package ch.zli.cospace.services;

import ch.zli.cospace.models.Booking;
import ch.zli.cospace.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    public void deleteAllBookingsofUser(Long id) throws NoSuchElementException {
        var user = userService.findUserById(id).orElseThrow();
        bookingRepository.deleteAllByCreator(user);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }
}
