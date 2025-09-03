package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.ReservationStatus;
import finalmission.domain.User;
import finalmission.domain.UserId;
import finalmission.domain.UserName;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationDetailResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.dto.response.ReservationCreateResponse;
import finalmission.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약가능한_책을_모두_조회한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);
        List<Book> books = List.of(book);

        List<AvailableBookResponse> result = List.of(AvailableBookResponse.from(book));

        when(bookService.findAll())
                .thenReturn(books);

        assertThat(reservationService.getAvailableBooks()).isEqualTo(result);
    }

    @Test
    void 책을_예약한다() {
        String userId = "userId";
        Long bookId = 1L;
        LocalDate reserveDate = LocalDate.now();
        ReservationCreateRequest request = new ReservationCreateRequest(bookId, reserveDate);

        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        Reservation reservation = Reservation.createReservation(user, book, reserveDate);
        ReservationCreateResponse result = ReservationCreateResponse.from(reservation);

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(bookService.findById(bookId))
                .thenReturn(book);
        when(reservationRepository.existsByUser_IdAndBook_IdAndReserveDate(any(), any(), any()))
                .thenReturn(false);
        when(reservationRepository.save(reservation))
                .thenReturn(reservation);

        assertThat(reservationService.reserveBook(userId, request)).isEqualTo(result);
    }

    @Test
    void 예약하려는_책의_예약가능수량이_부족하면_예외가_발생한다() {
        String userId = "userId";
        Long bookId = 1L;
        LocalDate reserveDate = LocalDate.now();
        ReservationCreateRequest request = new ReservationCreateRequest(bookId, reserveDate);

        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 1;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        book.adjustAvailableCount(1);

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(bookService.findById(bookId))
                .thenReturn(book);

        assertThatThrownBy(() -> reservationService.reserveBook(userId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_예약된_책을_예약하면_예외가_발생한다() {
        String userId = "userId";
        Long bookId = 1L;
        LocalDate reserveDate = LocalDate.now();
        ReservationCreateRequest request = new ReservationCreateRequest(bookId, reserveDate);

        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(bookService.findById(bookId))
                .thenReturn(book);
        when(reservationRepository.existsByUser_IdAndBook_IdAndReserveDate(any(), any(), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationService.reserveBook(userId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과거_날짜에_예약하면_예외가_발생한다() {
        String userId = "userId";
        Long bookId = 1L;
        LocalDate reserveDate = LocalDate.now().minusDays(1);
        ReservationCreateRequest request = new ReservationCreateRequest(bookId, reserveDate);

        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(bookService.findById(bookId))
                .thenReturn(book);
        when(reservationRepository.existsByUser_IdAndBook_IdAndReserveDate(any(), any(), any()))
                .thenReturn(false);

        assertThatThrownBy(() -> reservationService.reserveBook(userId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사용자의_모든_예약을_조회한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();
        Reservation reservation = Reservation.createReservation(user, book, reserveDate);

        List<MyReservationResponse> result = List.of(MyReservationResponse.from(reservation));

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findByUser_Id(any()))
                .thenReturn(List.of(reservation));

        assertThat(reservationService.getReservations(userId)).isEqualTo(result);
    }

    @Test
    void 사용자의_예약한건의_상세내역을_조회한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();

        Reservation reservation = Reservation.createReservation(user, book, reserveDate);
        Long existsId = 1L;

        MyReservationDetailResponse result = MyReservationDetailResponse.from(reservation);

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(existsId))
                .thenReturn(Optional.of(reservation));

        assertThat(reservationService.getReservation(userId, existsId)).isEqualTo(result);
    }

    @Test
    void 사용자의_예약한건의_상세내역_조회시_예약정보가_없다면_예외가_발생한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        Long notExistsId = 1000L;

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(notExistsId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservation(userId, notExistsId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 사용자의_예약한건의_상세내역_조회시_예약정보와_사용자정보가_다르면_예외가_발생한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        UserId differentUserId = UserId.from("differentUserId");
        User differentUser = User.createCrew(name, differentUserId);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();

        Reservation reservation = Reservation.createReservation(differentUser, book, reserveDate);
        Long existsId = 1L;

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(existsId))
                .thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.getReservation(userId, existsId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 예약한_책의_반납을_연장한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();

        Reservation reservation = Reservation.createReservation(user, book, reserveDate);
        Long existsId = 1L;

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(existsId))
                .thenReturn(Optional.of(reservation));

        MyReservationDetailResponse result = reservationService.extendReservation(userId, existsId);
        assertThat(result.returnDate()).isEqualTo(reserveDate.plusDays(6).plusDays(7));
    }

    @Test
    void 이미_반납연장한_책을_또_연장하면_예외가_발생한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();

        Reservation reservation = Reservation.createReservation(user, book, reserveDate);
        reservation.extendReturnDate();
        Long existsId = 1L;

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(existsId))
                .thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.extendReservation(userId, existsId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약한_책을_반납한다() {
        String userId = "userId";
        UserName name = UserName.from("name");
        UserId id = UserId.from(userId);
        User user = User.createCrew(name, id);

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        LocalDate reserveDate = LocalDate.now();

        Reservation reservation = Reservation.createReservation(user, book, reserveDate);
        Long existsId = 1L;

        when(userService.findByUserId(userId))
                .thenReturn(user);
        when(reservationRepository.findById(existsId))
                .thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(userId, existsId);

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RETURNED);
    }
}
