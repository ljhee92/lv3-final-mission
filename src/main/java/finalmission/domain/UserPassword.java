package finalmission.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassword {

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 10;

    private String password;

    public static UserPassword from(String password) {
        validateEmpty(password);
        validateLength(password);
        return new UserPassword(password);
    }

    private static void validateEmpty(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 비밀번호는 빈 값일 수 없습니다.");
        }
    }

    private static void validateLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 사용자의 비밀번호는 5자 이상 10자 이하여야 합니다.");
        }
    }
}
