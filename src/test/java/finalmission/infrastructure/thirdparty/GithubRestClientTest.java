package finalmission.infrastructure.thirdparty;

import finalmission.dto.response.GithubUserResponse;
import finalmission.dto.response.TokenResponse;
import finalmission.exception.ApiException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(GithubRestClient.class)
@TestPropertySource(properties = {
        "github.client-id=test-client-id",
        "github.client-secret=test-client-secret"
})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GithubRestClientTest {

    @Autowired
    private GithubRestClient githubRestClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void 엑세스_토큰을_반환한다() {
        String testCode = "test-code";
        String responseBody = """
                {
                    "access_token": "test-access-token"
                }
                """;

        server.expect(once(), requestTo(startsWith("https://github.com/login/oauth/access_token")))
                .andExpect(queryParam("client_id", "test-client-id"))
                .andExpect(queryParam("client_secret", "test-client-secret"))
                .andExpect(queryParam("code", testCode))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        TokenResponse response = githubRestClient.getAccessToken(testCode);

        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.accessToken()).isEqualTo("test-access-token")
        );
    }

    @Test
    void 액세스_토큰_요청시_깃허브_클라이언트_정보가_잘못되면_예외가_발생한다() {
        String testCode = "test-code";
        String responseBody = """
                {
                    "error": "incorrect_client_credentials",
                    "error_description": "The client_id and/or client_secret passed are incorrect.",
                    "error_uri": "/apps/managing-oauth-apps/troubleshooting-oauth-app-access-token-request-errors/#incorrect-client-credentials"
                }
                """;

        server.expect(once(), requestTo(startsWith("https://github.com/login/oauth/access_token")))
                .andExpect(queryParam("code", testCode))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> githubRestClient.getAccessToken(testCode))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void 액세스_토큰_요청시_깃허브_서버에러가_발생하면_예외가_발생한다() {
        String testCode = "test-code";

        server.expect(once(), requestTo(startsWith("https://github.com/login/oauth/access_token")))
                .andExpect(queryParam("client_id", "test-client-id"))
                .andExpect(queryParam("client_secret", "test-client-secret"))
                .andExpect(queryParam("code", testCode))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertThatThrownBy(() -> githubRestClient.getAccessToken(testCode))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void 액세스_토큰_요청시_연결이나_읽기가_지연되면_예외가_발생한다() {
        String testCode = "test-code";

        server.expect(once(), requestTo(startsWith("https://github.com/login/oauth/access_token")))
                .andExpect(queryParam("client_id", "test-client-id"))
                .andExpect(queryParam("client_secret", "test-client-secret"))
                .andExpect(queryParam("code", testCode))
                .andExpect(method(HttpMethod.POST))
                .andRespond(request -> {
                    throw new ResourceAccessException("time out");
                });

        assertThatThrownBy(() -> githubRestClient.getAccessToken(testCode))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                });
    }

    @Test
    void 유저정보를_반환한다() {
        String accessToken = "test-access-token";
        String responseBody = """
                {
                    "login": "test-login",
                    "name": "test-name"
                }
                """;

        server.expect(once(), requestTo(startsWith("https://api.github.com/user")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer " + accessToken))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        GithubUserResponse response = githubRestClient.getUser(accessToken);

        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.login()).isEqualTo("test-login"),
                () -> assertThat(response.name()).isEqualTo("test-name")
        );
    }

    @Test
    void 유저정보_반환시_액세스토큰이_잘못되면_예외가_발생한다() {
        String accessToken = "test-access-token";

        server.expect(once(), requestTo(startsWith("https://api.github.com/user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> githubRestClient.getUser(accessToken))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void 유저정보_반환시_깃허브_서버에러가_발생하면_예외가_발생한다() {
        String accessToken = "test-access-token";

        server.expect(once(), requestTo(startsWith("https://api.github.com/user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThatThrownBy(() -> githubRestClient.getUser(accessToken))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void 유저정보_반환시_연결이나_읽기가_지연되면_예외가_발생한다() {
        String accessToken = "test-access-token";

        server.expect(once(), requestTo(startsWith("https://api.github.com/user")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer " + accessToken))
                .andRespond(request -> {
                    throw new ResourceAccessException("time out");
                });

        assertThatThrownBy(() -> githubRestClient.getUser(accessToken))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> {
                    ApiException ex = (ApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                });
    }
}
