package finalmission.infrastructure.jwt;

import finalmission.domain.Role;
import finalmission.dto.request.LoginUser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret-key=testJwtSecretKeyTestJwtSecretKey",
        "jwt-validity=10000"
})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final JwtTokenProvider invalidSecretKeyJwtTokenProvider
            = new JwtTokenProvider("invalidSecretKeyInvalidSecretKey", 1000);

    @Test
    void 토큰이_생성된다() {
        String userId = "testUserId";
        String userName = "testUserName";
        Role role = Role.CREW;
        LoginUser loginUser = new LoginUser(userId, userName, role);

        String token = jwtTokenProvider.createToken(loginUser);

        assertThat(token).isNotNull();
    }

    @Test
    void 토큰으로_아이디를_조회한다() {
        String userId = "testUserId";
        String userName = "testUserName";
        Role role = Role.CREW;
        LoginUser loginUser = new LoginUser(userId, userName, role);

        String token = jwtTokenProvider.createToken(loginUser);

        assertThat(jwtTokenProvider.getUserIdFromToken(token)).isEqualTo(userId);
    }

    @Test
    void 유효하지_않은_토큰의_형식으로_아이디를_조회하면_예외가_발생한다() {
        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromToken(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 만료된_토큰으로_아이디를_조회하면_예외가_발생한다() {
        String expiredToken = Jwts.builder()
                .setSubject("testUserId")
                .setExpiration(new Date(new Date().getTime() - 1))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromToken(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void 잘못된_시크릿키로_아이디를_조회하면_예외가_발생한다() {
        String userId = "testUserId";
        String userName = "testUserName";
        Role crew = Role.CREW;
        LoginUser loginUser = new LoginUser(userId, userName, crew);

        String invalidSecretKeyToken = invalidSecretKeyJwtTokenProvider.createToken(loginUser);

        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromToken(invalidSecretKeyToken))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void 토큰으로_권한을_조회한다() {
        String userId = "testUserId";
        String userName = "testUserName";
        Role crew = Role.CREW;
        Role coach = Role.COACH;

        LoginUser loginUserOfCrew = new LoginUser(userId, userName, crew);
        LoginUser loginUserOfCoach = new LoginUser(userId, userName, coach);

        String tokenOfCrew = jwtTokenProvider.createToken(loginUserOfCrew);
        String tokenOfCoach = jwtTokenProvider.createToken(loginUserOfCoach);

        assertAll(
                () -> assertThat(jwtTokenProvider.getRoleFromToken(tokenOfCrew)).isEqualTo(crew),
                () -> assertThat(jwtTokenProvider.getRoleFromToken(tokenOfCoach)).isEqualTo(coach)
        );
    }

    @Test
    void 유효하지_않은_토큰의_형식으로_권한을_조회하면_예외가_발생한다() {
        assertThatThrownBy(() -> jwtTokenProvider.getRoleFromToken(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 만료된_토큰으로_권한을_조회하면_예외가_발생한다() {
        String expiredToken = Jwts.builder()
                .setSubject("testUserId")
                .claim("role", Role.CREW)
                .setExpiration(new Date(new Date().getTime() - 1))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.getRoleFromToken(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void 잘못된_시크릿키로_권한을_조회하면_예외가_발생한다() {
        String userId = "testUserId";
        String userName = "testUserName";
        Role crew = Role.CREW;
        LoginUser loginUser = new LoginUser(userId, userName, crew);

        String invalidSecretKeyToken = invalidSecretKeyJwtTokenProvider.createToken(loginUser);

        assertThatThrownBy(() -> jwtTokenProvider.getRoleFromToken(invalidSecretKeyToken))
                .isInstanceOf(SignatureException.class);
    }
}
