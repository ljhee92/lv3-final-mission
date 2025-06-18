package finalmission.application;

import finalmission.domain.User;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.LoginUser;
import finalmission.infrastructure.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserService userService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public String login(LoginRequest request) {
        User user = userService.findByEmail(request.email());
        userService.checkPassword(user, request.password());
        LoginUser loginUser = new LoginUser(user.getEmail(), user.getName(), user.getRole());
        return jwtTokenProvider.createToken(loginUser);
    }
}
