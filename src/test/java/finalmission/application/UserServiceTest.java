package finalmission.application;

import finalmission.domain.User;
import finalmission.domain.UserId;
import finalmission.domain.UserName;
import finalmission.dto.response.GithubUserResponse;
import finalmission.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 깃헙_정보로_사용자를_조회한다() {
        GithubUserResponse response = new GithubUserResponse("id", "name");
        UserName name = UserName.from("name");
        UserId id = UserId.from("id");
        User user = User.createCrew(name, id);

        when(userRepository.existsByUserId(id))
                .thenReturn(true);
        when(userRepository.findByUserId(id))
                .thenReturn(Optional.of(user));

        User foundUser = userService.findOrCreateUser(response);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void 깃헙_정보로_사용자를_조회할때_사용자가_없으면_생성하여_반환한다() {
        GithubUserResponse response = new GithubUserResponse("id", "name");
        UserName name = UserName.from("name");
        UserId id = UserId.from("id");
        User user = User.createCrew(name, id);

        when(userRepository.existsByUserId(id))
                .thenReturn(false);
        when(userRepository.save(user))
                .thenReturn(user);

        User createdUser = userService.findOrCreateUser(response);

        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    void ID로_사용자를_조회한다() {
        UserName name = UserName.from("name");
        UserId id = UserId.from("userId");
        User user = User.createCrew(name, id);

        when(userRepository.findByUserId(id))
                .thenReturn(Optional.of(user));

        User foundUser = userService.findByUserId("userId");

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void 존재하지_않는_ID로_사용자를_조회하면_예외가_발생한다() {
        when(userRepository.findByUserId(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUserId("notExistId"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 존재하는_ID로_사용자의_존재여부를_확인하면_true를_반환한다() {
        when(userRepository.existsByUserId(any()))
                .thenReturn(true);

        assertThat(userService.existsByUserId("userId")).isTrue();
    }

    @Test
    void 존재하지_않는_ID로_사용자의_존재여부를_확인하면_false를_반환한다() {
        when(userRepository.existsByUserId(any()))
                .thenReturn(false);

        assertThat(userService.existsByUserId("notExistId")).isFalse();
    }
}
