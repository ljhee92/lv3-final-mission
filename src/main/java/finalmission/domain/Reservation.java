package finalmission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private LocalDate reserveDate;

    private LocalTime reserveTime;

    private LocalDate returnDate;

    public static Reservation createReservation(User user, Book book, LocalDate reserveDate) {
        validateDate(reserveDate);
        return new Reservation(null, user, book, reserveDate, LocalTime.now(), reserveDate.plusDays(6));
    }

    private static void validateDate(LocalDate reserveDate) {
        if (reserveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 예약은 현재 날짜 이후만 가능합니다.");
        }
    }

    public boolean isSameUser(User user) {
        return user.equals(this.user);
    }

    public void extendReturnDate() {
        this.returnDate = this.returnDate.plusDays(7);
    }
}
