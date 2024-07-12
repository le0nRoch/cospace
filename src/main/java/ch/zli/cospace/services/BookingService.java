package ch.zli.cospace.services;

import ch.zli.cospace.models.Booking;
import ch.zli.cospace.repositories.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findByUserId(Long userId) throws NoSuchElementException {
        var user = userService.findUserById(userId).orElseThrow();
        return bookingRepository.findByCreator(user);
    }

    public void save(Booking booking) {
        var list  = new ArrayList<Booking>();
        list.add(booking);
        booking.getCreator().setBookings(list);
        bookingRepository.save(booking);
        userService.save(booking.getCreator());
    }

    public void update(Booking booking) {
        bookingRepository.save(booking);
    }
}
