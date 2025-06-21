package finalmission.fixture;

import finalmission.domain.User;
import finalmission.domain.UserEmail;
import finalmission.domain.UserName;
import finalmission.domain.UserPassword;
import finalmission.dto.request.LoginRequest;
import finalmission.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.stereotype.Component;

@Component
public class UserFixture {

    private final UserRepository userRepository;

    public UserFixture(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createBrown() {
        UserName name = UserName.from("브라운");
        UserEmail email = UserEmail.from("brown@email.com");
        UserPassword password = UserPassword.from("password");
        User brown = User.createCoach(name, email, password);

        return userRepository.save(brown);
    }

    public User createDuei() {
        UserName name = UserName.from("듀이");
        UserEmail email = UserEmail.from("duei@email.com");
        UserPassword password = UserPassword.from("password");
        User duei = User.createCrew(name, email, password);

        return userRepository.save(duei);
    }

    public String getToken(LoginRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .extract()
                .cookie("token");
    }
}
