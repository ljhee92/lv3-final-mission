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
public class UserId {

    private static final int MAX_ID_LENGTH = 39;

    private String userId;

    public static UserId from(String userId) {
        validateEmpty(userId);
        validateLength(userId);
        return new UserId(userId);
    }

    private static void validateEmpty(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 아이디는 빈 값일 수 없습니다.");
        }
    }

    private static void validateLength(String userId) {
        if (userId.isEmpty() || userId.length() > MAX_ID_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 아이디는 1자 이상 39자 이하여야 합니다.");
        }
    }
}
