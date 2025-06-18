package finalmission.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEmail {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private String email;

    public static UserEmail from(String email) {
        validateEmpty(email);
        validateLength(email);
        return new UserEmail(email);
    }

    private static void validateEmpty(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이메일은 빈 값일 수 없습니다.");
        }
    }

    private static void validateLength(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이메일 형식은 email@email.com이어야 합니다.");
        }
    }
}
