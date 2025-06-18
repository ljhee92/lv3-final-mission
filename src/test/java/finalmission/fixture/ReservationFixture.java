package finalmission.fixture;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.User;
import finalmission.repository.BookRepository;
import finalmission.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ReservationFixture {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    public ReservationFixture(ReservationRepository reservationRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }

    public Reservation createReservation(User user, Book book) {
        LocalDate reserveDate = LocalDate.now();
        LocalTime reserveTime = LocalTime.now().plusSeconds(1);
        Reservation reservation = Reservation.createReservation(user, book, reserveDate, reserveTime);
        book.adjustAvailableCount(1);
        bookRepository.save(book);
        return reservationRepository.save(reservation);
    }
}
