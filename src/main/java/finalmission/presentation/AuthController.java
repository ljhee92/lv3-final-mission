package finalmission.presentation;

import finalmission.application.AuthService;
import finalmission.dto.request.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${github.home-uri}")
    private String homeUri;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login/github")
    public ResponseEntity<Void> redirectToGithub() {
        String authorizeUrl = authService.getAuthorizeUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authorizeUrl))
                .build();
    }

    @GetMapping("/login/github/callback")
    public ResponseEntity<Void> getAccessToken(
            @RequestParam String code,
            HttpServletResponse response
    ) {
        String token = authService.login(code);
        createCookie(response, token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(homeUri))
                .build();
    }

    private void createCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginUser> checkLogin(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(loginUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        expireCookie(response);
        return  ResponseEntity.ok()
                .build();
    }

    private void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
