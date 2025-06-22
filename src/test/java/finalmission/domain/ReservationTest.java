package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest {

    @Test
    void 현재날짜_이전으로_예약을_생성하면_예외가_발생한다() {
        UserName name = UserName.from("듀이");
        UserId userId = UserId.from("duei");
        User crew = User.createCrew(name, userId);

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

        LocalDate reserveDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> Reservation.createReservation(crew, book, reserveDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약의_사용자와_동일한_사용자면_true를_반환한다() {
        UserName name = UserName.from("듀이");
        UserId userId = UserId.from("duei");
        User crew = User.createCrew(name, userId);

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

        Reservation reservation = Reservation.createReservation(crew, book, reserveDate);

        assertThat(reservation.isSameUser(crew)).isTrue();
    }

    @Test
    void 예약의_사용자와_동일한_사용자가_아니면_false를_반환한다() {
        UserName name = UserName.from("듀이");
        UserId userId = UserId.from("duei");
        User duei = User.createCrew(name, userId);

        UserName anotherName = UserName.from("브라운");
        UserId anotherUserId = UserId.from("brown");
        User brown = User.createCoach(anotherName, anotherUserId);

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

        Reservation reservation = Reservation.createReservation(duei, book, reserveDate);

        assertThat(reservation.isSameUser(brown)).isFalse();
    }

    @Test
    void 예약의_반납날짜를_연장한다() {
        UserName name = UserName.from("듀이");
        UserId userId = UserId.from("duei");
        User crew = User.createCrew(name, userId);

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

        Reservation reservation = Reservation.createReservation(crew, book, reserveDate);
        reservation.extendReturnDate();
        LocalDate expected = reserveDate.plusDays(6).plusDays(7);

        assertThat(reservation.getReturnDate()).isEqualTo(expected);
    }
}
