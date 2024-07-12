package ch.zli.cospace.repositories;

import ch.zli.cospace.models.Booking;
import ch.zli.cospace.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    void deleteAllByCreator(User creator);
}
