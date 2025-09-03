package finalmission.presentation;

import finalmission.application.AuthService;
import finalmission.domain.Role;
import finalmission.exception.AuthException;
import finalmission.infrastructure.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String uri = request.getRequestURI();
        String token = JwtTokenExtractor.extract(request);
        Role role = authService.findRoleByToken(token);
        if (uri.startsWith("/admin") && role != Role.COACH) {
            throw new AuthException("[ERROR] 접근 권한이 필요합니다.", HttpStatus.FORBIDDEN);
        }
        return true;
    }
}
