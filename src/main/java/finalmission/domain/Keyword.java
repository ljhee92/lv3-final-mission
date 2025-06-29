package finalmission.domain;

import finalmission.exception.ApiException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Keyword {

    private String keyword;

    public static Keyword from(String keyword) {
        validateEmpty(keyword);
        return new Keyword(keyword);
    }

    private static void validateEmpty(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new ApiException("[ERROR] 검색 키워드는 빈 값일 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword1 = (Keyword) o;
        return Objects.equals(keyword, keyword1.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(keyword);
    }
}
