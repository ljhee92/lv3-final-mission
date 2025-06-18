package finalmission;

import finalmission.domain.User;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.LoginUser;
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
