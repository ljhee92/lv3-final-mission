package finalmission.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

    private static final int MAX_ID_LENGTH = 39;

    private String userId;

    private UserId(String userId) {
        validateEmpty(userId);
        validateLength(userId);
        this.userId = userId;
    }

    public static UserId from(String userId) {
        return new UserId(userId);
    }

    private void validateEmpty(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 아이디는 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(String userId) {
        if (userId.isEmpty() || userId.length() > MAX_ID_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 아이디는 1자 이상 39자 이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
