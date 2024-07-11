package ch.zli.cospace.repositories;

import ch.zli.cospace.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
