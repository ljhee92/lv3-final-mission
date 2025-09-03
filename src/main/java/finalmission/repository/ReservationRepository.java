package finalmission.repository;

import finalmission.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser_Id(Long userId);

    boolean existsByUser_IdAndBook_IdAndReserveDate(Long userId, Long bookId, LocalDate reserveDate);
}
