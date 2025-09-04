package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserNameTest {

    @Test
    void 사용자의_이름이_널이면_디폴트_이름이_지정된다() {
        String name = null;

        UserName userName = UserName.from(name);

        assertThat(userName.getName()).startsWith("사용자");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 사용자의_이름이_빈값이면_디폴트_이름이_지정된다(String name) {
        UserName userName = UserName.from(name);

        assertThat(userName.getName()).startsWith("사용자");
    }

    @ParameterizedTest
    @ValueSource(strings = {"브라운", "듀이", "라이브러리"})
    void 사용자의_이름이_2자이상_5자이하면_예외가_발생하지_않는다(String name) {
        assertThatCode(() -> UserName.from(name))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"나", "안젤리나졸리"})
    void 사용자의_이름이_2자미만_5자초과이면_예외가_발생한다(String name) {
        assertThatThrownBy(() -> UserName.from(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
