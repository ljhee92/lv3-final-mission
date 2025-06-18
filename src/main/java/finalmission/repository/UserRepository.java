package finalmission.repository;

import finalmission.domain.User;
import finalmission.domain.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(UserEmail email);
}
