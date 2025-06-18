package finalmission.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserName {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 5;

    private String name;

    public static UserName from(String name) {
        validateEmpty(name);
        validateLength(name);
        return new UserName(name);
    }

    private static void validateEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 빈 값일 수 없습니다.");
        }
    }

    private static void validateLength(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 2자 이상 5자 이하여야 합니다.");
        }
    }
}
