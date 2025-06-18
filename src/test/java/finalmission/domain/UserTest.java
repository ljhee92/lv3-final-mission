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

        User crew = User.createCrew(name, email, password);

        assertThat(crew.getRole()).isEqualTo(Role.CREW);
    }

    @Test
    void 비밀번호가_같으면_true를_반환한다() {
        UserName name = UserName.from("듀이");
        UserEmail email = UserEmail.from("duei@email.com");
        UserPassword password = UserPassword.from("password");

        User crew = User.createCrew(name, email, password);

        UserPassword samePassword = UserPassword.from("password");

        assertThat(crew.isSamePassword(samePassword)).isTrue();
    }

    @Test
    void 비밀번호가_다르면_false를_반환한다() {
        UserName name = UserName.from("듀이");
        UserEmail email = UserEmail.from("duei@email.com");
        UserPassword password = UserPassword.from("password");

        User crew = User.createCrew(name, email, password);

        UserPassword differentPassword = UserPassword.from("password2");

        assertThat(crew.isSamePassword(differentPassword)).isFalse();
    }
}
