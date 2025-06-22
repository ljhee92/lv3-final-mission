package finalmission.application;

import finalmission.domain.Role;
import finalmission.domain.User;
import finalmission.domain.UserId;
import finalmission.domain.UserName;
import finalmission.dto.request.LoginUser;
import finalmission.dto.response.GithubUserResponse;
import finalmission.exception.AuthException;
import finalmission.infrastructure.jwt.JwtTokenProvider;
import finalmission.infrastructure.thirdparty.GithubRestClient;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final GithubRestClient githubRestClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(
            GithubRestClient githubRestClient,
            JwtTokenProvider jwtTokenProvider,
            UserService userService
    ) {
        this.githubRestClient = githubRestClient;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public String getAuthorizeUrl() {
        return githubRestClient.getAuthorizeUrl();
    }

    public String login(String code) {
        String accessToken = createAccessToken(code);
        GithubUserResponse response = githubRestClient.getUser(accessToken);
        User user = findUser(response);
        LoginUser loginUser = new LoginUser(user.getUserId(), user.getName(), user.getRole());
        return jwtTokenProvider.createToken(loginUser);
    }

    private String createAccessToken(String code) {
        return githubRestClient.getAccessToken(code)
                .accessToken();
    }

    private User findUser(GithubUserResponse response) {
        String name = response.name();
        String id = response.login();
        if (!userService.existsByUserId(id)) {
            User userWithoutId = User.createCrew(UserName.from(name), UserId.from(id));
            return userService.save(userWithoutId);
        }
        return userService.findByUserId(id);
    }

    public LoginUser findLoginUserByToken(String token) {
        checkTokenValidation(token);
        String userIdFromToken = jwtTokenProvider.getUserIdFromToken(token);
        User user = userService.findByUserId(userIdFromToken);
        return new LoginUser(user.getUserId(), user.getName(), user.getRole());
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
