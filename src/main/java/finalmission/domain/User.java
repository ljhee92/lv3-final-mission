package finalmission.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    UserName name;

    @Embedded
    UserEmail email;

    @Embedded
    UserPassword password;

    @Enumerated(EnumType.STRING)
    Role role;

    public static User createCoach(UserName name, UserEmail email, UserPassword password) {
        return new User(null, name, email, password, Role.COACH);
    }

    public static User createCrew(UserName name, UserEmail email, UserPassword password) {
        return new User(null, name, email, password, Role.CREW);
    }
}
