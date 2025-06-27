package finalmission.domain;

import finalmission.exception.ApiException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KeywordTest {

    @Test
    void 키워드가_널이면_예외가_발생한다() {
        String keyword = null;

        assertThatThrownBy(() -> Keyword.from(keyword))
                .isInstanceOf(ApiException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 키워드가_빈값이면_예외가_발생한다(String keyword) {
        assertThatThrownBy(() -> Keyword.from(keyword))
                .isInstanceOf(ApiException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"오브젝트", "좋은코드 나쁜코드"})
    void 키워드가_널이거나_빈값이아니면_예외가_발생하지_않는다(String keyword) {
        assertThatCode(() -> Keyword.from(keyword))
                .doesNotThrowAnyException();
    }
}