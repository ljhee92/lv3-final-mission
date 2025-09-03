package finalmission.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotNull(message = "책 번호는 필수입니다.")
        Long bookId,

        @NotNull(message = "예약 일자는 필수입니다.")
        LocalDate reserveDate
) {
}
