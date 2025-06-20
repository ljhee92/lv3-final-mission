package finalmission;

import finalmission.domain.Book;
import finalmission.domain.Reservation;
import finalmission.domain.User;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.LoginUser;
import finalmission.dto.request.ReservationCreateRequest;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.dto.response.MyReservationResponse;
import finalmission.fixture.BookFixture;
import finalmission.fixture.ReservationFixture;
import finalmission.fixture.UserFixture;
import io.restassured.RestAssured;
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
import static org.hamcrest.Matchers.notNullValue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {

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
    void 이메일과_비밀번호로_로그인하면_토큰을_반환한다() {
        User duei = userFixture.createDuei();
        LoginRequest request = new LoginRequest(duei.getEmail(), duei.getPassword());

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .cookie("token", notNullValue())
                .log().all();
    }

    @Test
    void 존재하지_않는_이메일로_로그인하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest request = new LoginRequest("brown@email.com", duei.getPassword());

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all();
    }

    @Test
    void 일치하지_않은_비밀번호로_로그인하면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginRequest request = new LoginRequest(duei.getEmail(), "notMyPass");

        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    void 로그인된_인증정보를_확인한다() {
        User duei = userFixture.createDuei();
        LoginRequest request = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = getToken(request);
        LoginUser loginUser = new LoginUser(duei.getEmail(), duei.getName(), duei.getRole());

        RestAssured.given()
                .contentType("application/json")
                .body(loginUser)
                .cookie("token", token)
                .when()
                .get("/login/check")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .log().all();
    }

    @Test
    void 쿠키가_존재하지_않으면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginUser loginUser = new LoginUser(duei.getEmail(), duei.getName(), duei.getRole());

        RestAssured.given()
                .contentType("application/json")
                .body(loginUser)
                .when()
                .get("/login/check")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .log().all();
    }

    @Test
    void 토큰이_존재하지_않으면_예외가_발생한다() {
        User duei = userFixture.createDuei();
        LoginUser loginUser = new LoginUser(duei.getEmail(), duei.getName(), duei.getRole());

        RestAssured.given()
                .contentType("application/json")
                .body(loginUser)
                .cookie("notToken", "notToken")
                .when()
                .get("/login/check")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .log().all();
    }

    @Test
    void 로그아웃한다() {
        User duei = userFixture.createDuei();
        LoginRequest request = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = getToken(request);

        RestAssured.given()
                .contentType("application/json")
                .cookie("token", token)
                .when()
                .post("/logout")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    void 관리자가_도서를_조회한다() {
        User brown = userFixture.createBrown();
        LoginRequest request = new LoginRequest(brown.getEmail(), brown.getPassword());
        String token = getToken(request);

        RestAssured.given()
                .contentType("application/json")
                .cookie("token", token)
                .param("keyword", "오브젝트")
                .when()
                .get("/admin/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(10))
                .log().all();
    }

    @Test
    void 관리자가_도서조회시_키워드를_입력하지_않으면_예외가_발생한다() {
        User brown = userFixture.createBrown();
        LoginRequest request = new LoginRequest(brown.getEmail(), brown.getPassword());
        String token = getToken(request);

        RestAssured.given()
                .contentType("application/json")
                .cookie("token", token)
                .param("keyword", "")
                .when()
                .get("/admin/books")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    void 관리자가_도서를_등록한다() {
        User brown = userFixture.createBrown();
        LoginRequest loginRequest = new LoginRequest(brown.getEmail(), brown.getPassword());
        String token = getToken(loginRequest);

        BookCreateRequest request = new BookCreateRequest(
                "오브젝트",
                "조영호",
                "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg",
                "위키북스",
                LocalDate.of(2019, 6, 17),
                "9791158391409",
                "오브젝트설명",
                2,
                LocalDate.now()
        );

        RestAssured.given()
                .contentType("application/json")
                .cookie("token", token)
                .body(request)
                .when()
                .post("/admin/books")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", is(4))
                .log().all();
    }

    @Test
    void 사용자가_예약가능한_도서를_조회한다() {
        User duei = userFixture.createDuei();
        LoginRequest loginRequest = new LoginRequest(duei.getEmail(), duei.getPassword());
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        List<AvailableBookResponse> responses = List.of(AvailableBookResponse.from(book1));

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();

        ReservationCreateRequest request = new ReservationCreateRequest(
                book1.getId(),
                LocalDate.now()
        );

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);
        reservationFixture.createReservation(duei, book1);

        ReservationCreateRequest request = new ReservationCreateRequest(
                book1.getId(),
                LocalDate.now()
        );

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);
        List<MyReservationResponse> responses = List.of(MyReservationResponse.from(reservation));

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        Reservation reservation = reservationFixture.createReservation(duei, book1);

        RestAssured.given()
                .contentType("application/json")
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
        String token = getToken(loginRequest);

        Book book1 = bookFixture.createBook1();
        reservationFixture.createReservation(duei, book1);

        User brown = userFixture.createBrown();
        Reservation reservationOfBrown = reservationFixture.createReservation(brown, book1);

        RestAssured.given()
                .contentType("application/json")
                .cookie("token", token)
                .pathParam("id", reservationOfBrown.getId())
                .when()
                .delete("/reservations/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private String getToken(LoginRequest request) {
        return RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .extract()
                .cookie("token");
    }
}
