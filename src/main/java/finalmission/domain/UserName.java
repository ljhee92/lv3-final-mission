package finalmission.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserName {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 5;

    private String name;

    private UserName(String name) {
        validateEmpty(name);
        validateLength(name);
        this.name = name;
    }

    public static UserName from(String name) {
        return new UserName(name);
    }

    private void validateEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 사용자의 이름은 2자 이상 5자 이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
