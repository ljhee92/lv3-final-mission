package finalmission.infrastructure.thirdparty;

import finalmission.dto.response.GithubUserResponse;
import finalmission.dto.response.TokenResponse;
import finalmission.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubRestClient {

    private static final String GITHUB_LOGIN_URL = "https://github.com/login/oauth";
    private static final String AUTHORIZE_URI = "/authorize";
    private static final String ACCESS_URI = "/access_token";
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String USER_URI = "/user";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestClient loginRestClient;
    private final RestClient apiRestClient;

    public GithubRestClient(
            @Value("${github.client-id}") String clientId,
            @Value("${github.client-secret}") String clientSecret,
            @Value("${github.redirect-uri}") String redirectUri,
            RestClient.Builder builder
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.loginRestClient = builder.baseUrl(GITHUB_LOGIN_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(new GithubErrorHandler())
                .build();
        this.apiRestClient = builder.baseUrl(GITHUB_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(new GithubErrorHandler())
                .build();
    }

    public String getAuthorizeUrl() {
        return GITHUB_LOGIN_URL + AUTHORIZE_URI
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=read:user+user:email";
    }

    public TokenResponse getAccessToken(String code) {
        return ApiCallHandler.execute(() -> {
            TokenResponse response = loginRestClient.post()
                    .uri(uriBuilder -> uriBuilder.path(ACCESS_URI)
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .queryParam("code", code)
                            .build())
                    .retrieve()
                    .body(TokenResponse.class);

            if (response.accessToken() == null) {
                throw new ApiException("[ERROR] 깃허브 액세스 토큰을 받아오는 데 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return response;
        }, "[ERROR] 깃허브 API 통신 중 문제 발생");
    }

    public GithubUserResponse getUser(String accessToken) {
        return ApiCallHandler.execute(() ->
                apiRestClient.get()
                    .uri(uriBuilder -> uriBuilder.path(USER_URI)
                            .build())
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(GithubUserResponse.class)
                , "[ERROR] 깃허브 API 통신 중 문제 발생");
    }
}
