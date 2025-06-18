package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

    @Test
    void 코치를_생성한다() {
        UserName name = UserName.from("브라운");
        UserEmail email = UserEmail.from("admin@email.com");
        UserPassword password = UserPassword.from("password");

        User admin = User.createCoach(name, email, password);

        assertThat(admin.getRole()).isEqualTo(Role.COACH);
    }

    @Test
    void 크루를_생성한다() {
        UserName name = UserName.from("듀이");
        UserEmail email = UserEmail.from("duei@email.com");
        UserPassword password = UserPassword.from("password");

        User admin = User.createCrew(name, email, password);

        assertThat(admin.getRole()).isEqualTo(Role.CREW);
    }
}
