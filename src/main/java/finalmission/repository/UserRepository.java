package finalmission.repository;

import finalmission.domain.User;
import finalmission.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(UserId userId);

    boolean existsByUserId(UserId userId);
}
