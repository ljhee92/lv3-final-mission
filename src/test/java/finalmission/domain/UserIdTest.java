package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserIdTest {

    @Test
    void 사용자의_아이디가_널이면_예외가_발생한다() {
        String id = null;

        assertThatThrownBy(() -> UserId.from(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 사용자의_아이디가_빈값이면_예외가_발생한다(String userId) {
        assertThatThrownBy(() -> UserId.from(userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"브라운", "듀이", "일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구"})
    void 사용자의_아이디가_1자이상_39자이하면_예외가_발생하지_않는다(String userId) {
        assertThatCode(() -> UserId.from(userId))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십"})
    void 사용자의_아이디가_39자초과이면_예외가_발생한다(String userId) {
        assertThatThrownBy(() -> UserId.from(userId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
