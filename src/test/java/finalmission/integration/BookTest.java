package finalmission.integration;

import finalmission.domain.User;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.request.LoginRequest;
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

import static org.hamcrest.Matchers.is;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserFixture userFixture;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 관리자가_도서를_조회한다() {
        User brown = userFixture.createBrown();
        LoginRequest request = new LoginRequest(brown.getEmail(), brown.getPassword());
        String token = userFixture.getToken(request);

        RestAssured.given()
                .contentType(ContentType.JSON)
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
        String token = userFixture.getToken(request);

        RestAssured.given()
                .contentType(ContentType.JSON)
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
        String token = userFixture.getToken(loginRequest);

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
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when()
                .post("/admin/books")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", is(4))
                .log().all();
    }
}
