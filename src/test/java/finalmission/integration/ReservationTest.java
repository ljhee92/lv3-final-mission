package finalmission.integration;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.User;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.fixture.BookFixture;
import finalmission.fixture.ReservationFixture;
import finalmission.fixture.UserFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserFixture userFixture;

    @Autowired
    BookFixture bookFixture;

    @Autowired
    ReservationFixture reservationFixture;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 사용자가_예약가능한_도서를_조회한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        List<AvailableBookResponse> responses = List.of(AvailableBookResponse.from(book1));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .get("/reservations/available")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", response -> is(responses.size()))
                .log().all();
    }

    @Test
    void 사용자가_도서를_예약한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();

        ReservationCreateRequest request = new ReservationCreateRequest(
                book1.getId(),
                LocalDate.now()
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when()
                .post("/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", is(5))
                .log().all();
    }

    @Test
    void 사용자가_예약가능수량이_0인_도서를_예약하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);
        reservationFixture.createReservation(duei, book1);

        ReservationCreateRequest request = new ReservationCreateRequest(
                book1.getId(),
                LocalDate.now()
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when()
                .post("/reservations")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 사용자가_예약_리스트를_조회한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);
        List<MyReservationResponse> responses = List.of(MyReservationResponse.from(reservation));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", response -> is(responses.size()))
                .log().all();
    }

    @Test
    void 사용자가_예약_상세정보를_조회한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservation.getId())
                .when()
                .get("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4))
                .log().all();
    }

    @Test
    void 사용자가_본인의_예약이_아닌_정보를_조회하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservationOfBrown.getId())
                .when()
                .get("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 사용자가_자신의_예약을_연장한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservation.getId())
                .when()
                .put("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4))
                .log().all();
    }

    @Test
    void 사용자가_본인의_예약이_아닌_예약을_연장하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservationOfBrown.getId())
                .when()
                .put("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 사용자가_자신의_예약을_취소한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservation.getId())
                .when()
                .delete("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    void 사용자가_본인의_예약이_아닌_예약을_취소하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = userFixture.getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("id", reservationOfBrown.getId())
                .when()
                .delete("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
