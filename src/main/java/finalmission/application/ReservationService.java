package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.User;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationDetailResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.dto.response.ReservationCreateResponse;
import finalmission.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    public MyReservationDetailResponse getReservation(String email, Long reservationId) {
        User user = userService.findByEmail(email);
        Reservation reservation = findById(reservationId);
        validateUserOfReservation(reservation, user);
        return MyReservationDetailResponse.from(reservation);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약 정보가 없습니다."));
    }

    private void validateUserOfReservation(Reservation reservation, User user) {
        if (!reservation.isSameUser(user)) {
            throw new NoSuchElementException("[ERROR] 예약 정보와 사용자 정보가 다릅니다.");
        }
    }

    @Transactional
    public MyReservationDetailResponse extendReservation(String email, Long reservationId) {
        User user = userService.findByEmail(email);
        Reservation reservation = findById(reservationId);
        validateUserOfReservation(reservation, user);
        reservation.extendReturnDate();
        return MyReservationDetailResponse.from(reservation);
    }

    @Transactional
    public void cancelReservation(String email, Long reservationId) {
        User user = userService.findByEmail(email);
        Reservation reservation = findById(reservationId);
        validateUserOfReservation(reservation, user);
        reservationRepository.delete(reservation);
    }
}
