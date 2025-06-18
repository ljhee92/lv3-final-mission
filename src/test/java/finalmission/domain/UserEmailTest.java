package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserEmailTest {

    @Test
    void 사용자의_이메일이_널이면_예외가_발생한다() {
        String email = null;

        assertThatThrownBy(() -> UserEmail.from(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 사용자의_이메일이_빈값이면_예외가_발생한다(String email) {
        assertThatThrownBy(() -> UserEmail.from(email))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"brown@email.com", "duei@email.com", "library@email.com"})
    void 사용자의_이메일이_형식에_맞다면_예외가_발생하지_않는다(String name) {
        assertThatCode(() -> UserEmail.from(name))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"brown@emailcom", "duei.emailcom", "libraryemail.com"})
    void 사용자의_이름이_형식에_맞지않으면_예외가_발생한다(String email) {
        assertThatThrownBy(() -> UserName.from(email))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
