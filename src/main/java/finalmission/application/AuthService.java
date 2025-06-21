package finalmission.application;

import finalmission.domain.Role;
import finalmission.domain.User;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.LoginUser;
import finalmission.exception.AuthException;
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

    public LoginUser findLoginUserByToken(String token) {
        checkTokenValidation(token);
        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        User user = userService.findByEmail(emailFromToken);
        return new LoginUser(user.getEmail(), user.getName(), user.getRole());
    }

    private void checkTokenValidation(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthException("[ERROR] 유효하지 않은 토큰입니다.");
        }
    }

    public Role findRoleByToken(String token) {
        checkTokenValidation(token);
        return jwtTokenProvider.getRoleFromToken(token);
    }
}
