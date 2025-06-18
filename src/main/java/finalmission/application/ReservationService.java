package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.User;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.dto.response.ReservationCreateResponse;
import finalmission.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final BookService bookService;
    private final UserService userService;
    private final ReservationRepository reservationRepository;

    public ReservationService(
            BookService bookService,
            UserService userService,
            ReservationRepository reservationRepository
    ) {
        this.bookService = bookService;
        this.userService = userService;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableBookResponse> getAvailableBooks() {
        List<Book> books = bookService.findAll();
        return books.stream()
                .map(AvailableBookResponse::from)
                .toList();
    }

    @Transactional
    public ReservationCreateResponse reserveBook(String email, ReservationCreateRequest request) {
        User user = userService.findByEmail(email);
        Book book = bookService.findById(request.bookId());

        Reservation reservationWithoutId = Reservation.createReservation(
                user, book, request.reserveDate(), request.reserveTime()
        );
        Reservation reservationWithId = reservationRepository.save(reservationWithoutId);
        return ReservationCreateResponse.from(reservationWithId);
    }

    public List<MyReservationResponse> getReservations(String email) {
        User user = userService.findByEmail(email);
        List<Reservation> reservations = reservationRepository.findByUser_Id(user.getId());
        return reservations.stream()
                .map(MyReservationResponse::from)
                .toList();
    }
}
