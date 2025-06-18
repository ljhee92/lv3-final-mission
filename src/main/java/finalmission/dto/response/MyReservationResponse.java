package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationResponse(
        Long id,

        String title,

        String author,

        String publisher,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reserveDate,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime returnDate
) {

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getBook().getTitle(),
                reservation.getBook().getAuthor(),
                reservation.getBook().getPublisher(),
                reservation.getReserveDate(),
                reservation.getReserveTime()
        );
    }
}
