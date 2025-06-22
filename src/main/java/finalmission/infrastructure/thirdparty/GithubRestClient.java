package finalmission.infrastructure.thirdparty;

import finalmission.dto.response.GithubUserResponse;
import finalmission.dto.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@PropertySource("classpath:secure.properties")
public class GithubRestClient {

    private static final String GITHUB_LOGIN_URL = "https://github.com/login/oauth";
    private static final String AUTHORIZE_URI = "/authorize";
    private static final String REDIRECT_URI = "http://localhost:8080/login/github/callback";
    private static final String ACCESS_URI = "/access_token";
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String USER_URI = "/user";

    private final String clientId;
    private final String clientSecret;
    private final RestClient loginRestClient;
    private final RestClient apiRestClient;

    public GithubRestClient(
            @Value("${github.client-id}") String clientId,
            @Value("${github.client-secret}") String clientSecret,
            RestClient.Builder builder
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginRestClient = builder.baseUrl(GITHUB_LOGIN_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.apiRestClient = builder.baseUrl(GITHUB_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getAuthorizeUrl() {
        return GITHUB_LOGIN_URL + AUTHORIZE_URI
                + "?client_id=" + clientId
                + "&redirect_uri=" + REDIRECT_URI
                + "&scope=read:user+user:email";
    }

    public TokenResponse getAccessToken(String code) {
        return loginRestClient.post()
                .uri(uriBuilder -> uriBuilder.path(ACCESS_URI)
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .body(TokenResponse.class);
    }

    public GithubUserResponse getUser(String accessToken) {
        return apiRestClient.get()
                .uri(uriBuilder -> uriBuilder.path(USER_URI)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(GithubUserResponse.class);
    }
}
