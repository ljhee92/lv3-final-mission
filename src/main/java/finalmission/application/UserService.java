package finalmission.application;

import finalmission.domain.User;
import finalmission.domain.UserId;
import finalmission.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(String userId) {
        UserId id = UserId.from(userId);
        return userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 사용자가 존재하지 않습니다."));
    }

    public boolean existsByUserId(String userId) {
        UserId id = UserId.from(userId);
        return userRepository.existsByUserId(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
