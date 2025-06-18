package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateResponse(
        Long id,

        String name,

        Long bookId,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reserveDate,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime reserveTime
) {

    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(
                reservation.getId(),
                reservation.getUser().getName(),
                reservation.getBook().getId(),
                reservation.getReserveDate(),
                reservation.getReserveTime()
        );
    }
}
