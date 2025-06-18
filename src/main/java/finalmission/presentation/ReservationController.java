package finalmission.presentation;

import finalmission.application.ReservationService;
import finalmission.dto.request.LoginUser;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.ReservationCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/reservations")
    public ResponseEntity<ReservationCreateResponse> reserveBook(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody @Valid ReservationCreateRequest request
            ) {
        ReservationCreateResponse response = reservationService.reserveBook(loginUser.email(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
