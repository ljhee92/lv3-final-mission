package finalmission.presentation;

import finalmission.application.AuthService;
import finalmission.dto.request.LoginRequest;
import finalmission.dto.request.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        String token = authService.login(request);
        createSession(token, response);
        return ResponseEntity.ok()
                .build();
    }

    private void createSession(String token, HttpServletResponse response) {
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
