package finalmission.presentation;

import finalmission.application.ReservationService;
import finalmission.dto.request.LoginUser;
import finalmission.dto.response.AvailableBookResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations/available")
    public ResponseEntity<List<AvailableBookResponse>> getAvailableBooks(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<AvailableBookResponse> responses = reservationService.getAvailableBooks();
        return ResponseEntity.ok(responses);
    }
}
