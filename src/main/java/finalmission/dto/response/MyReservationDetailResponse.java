package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Reservation;

import java.time.LocalDate;

public record MyReservationDetailResponse(
        Long id,

        BookResponse bookResponse,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reserveDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate returnDate,

        String status
) {

    public static MyReservationDetailResponse from(Reservation reservation) {
        return new MyReservationDetailResponse(
                reservation.getId(),
                BookResponse.from(reservation.getBook()),
                reservation.getReserveDate(),
                reservation.getReturnDate(),
                reservation.getStatus().getName()
        );
    }
}
