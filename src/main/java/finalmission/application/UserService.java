package finalmission.application;

import finalmission.domain.User;
import finalmission.domain.UserEmail;
import finalmission.domain.UserPassword;
import finalmission.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        UserEmail userEmail = UserEmail.from(email);
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 이메일의 사용자가 존재하지 않습니다."));
    }

    public void checkPassword(User user, String password) {
        UserPassword userPassword = UserPassword.from(password);
        if (!user.isSamePassword(userPassword)) {
            throw new IllegalArgumentException("[ERROR] 잘못된 비밀번호입니다.");
        }
    }
}
