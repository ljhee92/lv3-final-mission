package finalmission.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Getter
    private Role role;

    public static User createCoach(UserName name, UserId userId) {
        return new User(null, name, userId, Role.COACH);
    }

    public static User createCrew(UserName name, UserId userId) {
        return new User(null, name, userId, Role.CREW);
    }

    public String getName() {
        return name.getName();
    }

    public String getUserId() {
        return userId.getUserId();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (id != null && user.id != null) {
            return Objects.equals(id, user.id);
        }
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hashCode(id) : Objects.hashCode(userId);
    }
}
