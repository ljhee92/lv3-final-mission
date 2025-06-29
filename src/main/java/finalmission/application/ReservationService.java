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
    public ReservationCreateResponse reserveBook(String userId, ReservationCreateRequest request) {
        User user = userService.findByUserId(userId);
        Book book = bookService.findById(request.bookId());
        book.checkAvailableCount();

        if (reservationRepository.existsByUser_IdAndBook_IdAndReserveDate(user.getId(), book.getId(), request.reserveDate())) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 책입니다.");
        }
        Reservation reservationWithoutId = Reservation.createReservation(user, book, request.reserveDate());
        book.adjustAvailableCount(1);
        Reservation reservationWithId = reservationRepository.save(reservationWithoutId);
        return ReservationCreateResponse.from(reservationWithId);
    }

    public List<MyReservationResponse> getReservations(String userId) {
        User user = userService.findByUserId(userId);
        List<Reservation> reservations = reservationRepository.findByUser_Id(user.getId());
        return reservations.stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    public MyReservationDetailResponse getReservation(String userId, Long reservationId) {
        User user = userService.findByUserId(userId);
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
    public MyReservationDetailResponse extendReservation(String userId, Long reservationId) {
        User user = userService.findByUserId(userId);
        Reservation reservation = findById(reservationId);
        validateUserOfReservation(reservation, user);
        if (!reservation.isExtendable()) {
            throw new IllegalArgumentException("[ERROR] 반납 연장은 1회만 가능합니다.");
        }
        reservation.extendReturnDate();
        return MyReservationDetailResponse.from(reservation);
    }

    @Transactional
    public void cancelReservation(String userId, Long reservationId) {
        User user = userService.findByUserId(userId);
        Reservation reservation = findById(reservationId);
        validateUserOfReservation(reservation, user);
        reservation.cancel();
    }
}
