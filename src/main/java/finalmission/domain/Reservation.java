package finalmission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Reservation(User user, Book book, LocalDate reserveDate) {
        validateDate(reserveDate);
        this.user = user;
        this.book = book;
        this.reserveDate = reserveDate;
        this.reserveTime = LocalTime.now();
        this.returnDate = reserveDate.plusDays(6);
        this.status = ReservationStatus.RESERVED;
    }

    public static Reservation createReservation(User user, Book book, LocalDate reserveDate) {
        return new Reservation(user, book, reserveDate);
    }

    private void validateDate(LocalDate reserveDate) {
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

    public boolean isExtendable() {
        return this.returnDate.equals(this.reserveDate.plusDays(6));
    }

    public void cancel() {
        this.status = ReservationStatus.RETURNED;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        if (id != null && reservation.id != null) {
            return Objects.equals(id, reservation.id);
        }
        return Objects.equals(user, reservation.user)
                && Objects.equals(book, reservation.book)
                && Objects.equals(reserveDate, reservation.reserveDate);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hashCode(id);
        }
        return Objects.hash(user, book, reserveDate);
    }
}
