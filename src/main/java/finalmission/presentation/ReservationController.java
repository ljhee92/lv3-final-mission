package finalmission.presentation;

import finalmission.application.ReservationService;
import finalmission.dto.request.LoginUser;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationDetailResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.dto.response.ReservationCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        ReservationCreateResponse response = reservationService.reserveBook(loginUser.userId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<MyReservationResponse>> getReservations(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<MyReservationResponse> responses = reservationService.getReservations(loginUser.userId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<MyReservationDetailResponse> getReservation(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(required = false) Long id
    ) {
        MyReservationDetailResponse response = reservationService.getReservation(loginUser.userId(), id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<MyReservationDetailResponse> updateReservation(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(required = false) Long id
    ) {
        MyReservationDetailResponse response = reservationService.extendReservation(loginUser.userId(), id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable(required = false) Long id
    ) {
        reservationService.cancelReservation(loginUser.userId(), id);
        return ResponseEntity.noContent().build();
    }
}
