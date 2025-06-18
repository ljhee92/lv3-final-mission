package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserPasswordTest {

    @Test
    void 사용자의_비밀번호가_널이면_예외가_발생한다() {
        String password = null;

        assertThatThrownBy(() -> UserPassword.from(password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 사용자의_비밀번호가_빈값이면_예외가_발생한다(String password) {
        assertThatThrownBy(() -> UserPassword.from(password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pass1", "password12"})
    void 사용자의_비밀번호가_5자이상_10자이하면_예외가_발생하지_않는다(String password) {
        assertThatCode(() -> UserPassword.from(password))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"pass", "password123"})
    void 사용자의_비밀번호가_5자미만_10자초과이면_예외가_발생한다(String password) {
        assertThatThrownBy(() -> UserPassword.from(password))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
