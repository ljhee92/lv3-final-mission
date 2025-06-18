package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Reservation;

import java.time.LocalDate;

public record MyReservationDetailResponse(
        Long id,

        String title,

        String author,

        String image,

        String publisher,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate pubdate,

        String isbn,

        String description,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reserveDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate returnDate
) {

    public static MyReservationDetailResponse from(Reservation reservation) {
        return new MyReservationDetailResponse(
                reservation.getId(),
                reservation.getBook().getTitle(),
                reservation.getBook().getAuthor(),
                reservation.getBook().getImage(),
                reservation.getBook().getPublisher(),
                reservation.getBook().getPubdate(),
                reservation.getBook().getIsbn(),
                reservation.getBook().getDescription(),
                reservation.getReserveDate(),
                reservation.getReturnDate()
        );
    }
}
