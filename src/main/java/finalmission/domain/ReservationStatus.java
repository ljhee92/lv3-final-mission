package finalmission.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    RESERVED("예약"),
    RETURNED("반납")
    ;

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }
}
