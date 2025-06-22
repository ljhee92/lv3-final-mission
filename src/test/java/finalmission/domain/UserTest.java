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
        UserId userId = UserId.from("brown");

        User admin = User.createCoach(name, userId);

        assertThat(admin.getRole()).isEqualTo(Role.COACH);
    }

    @Test
    void 크루를_생성한다() {
        UserName name = UserName.from("듀이");
        UserId userId = UserId.from("duei");

        User crew = User.createCrew(name, userId);

        assertThat(crew.getRole()).isEqualTo(Role.CREW);
    }
}
