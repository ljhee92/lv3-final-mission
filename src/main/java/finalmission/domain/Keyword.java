package finalmission.domain;

import finalmission.exception.NaverApiException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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
            throw new NaverApiException("[ERROR] 검색 키워드는 빈 값일 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
