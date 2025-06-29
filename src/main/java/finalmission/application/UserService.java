package finalmission.application;

import finalmission.domain.User;
import finalmission.domain.UserId;
import finalmission.domain.UserName;
import finalmission.dto.response.GithubUserResponse;
import finalmission.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findOrCreateUser(GithubUserResponse response) {
        if (existsByUserId(response.login())) {
            return findByUserId(response.login());
        }
        return createCrew(response);
    }

    @Transactional
    public User createCrew(GithubUserResponse response) {
        UserName name = UserName.from(response.name());
        UserId id = UserId.from(response.login());
        User userWithoutId = User.createCrew(name, id);
        return userRepository.save(userWithoutId);
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
}
