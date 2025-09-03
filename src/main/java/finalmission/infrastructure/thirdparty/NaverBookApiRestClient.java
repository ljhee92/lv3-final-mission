package finalmission.infrastructure.thirdparty;

import finalmission.domain.Keyword;
import finalmission.dto.response.ApiBookResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NaverBookApiRestClient implements BookApiRestClient {

    private static final String NAVER_URL = "https://openapi.naver.com";
    private static final String SEARCH_URI = "/v1/search/book.json";

    private final RestClient restClient;

    public NaverBookApiRestClient(
            @Value("${naver.client-id}") String clientId,
            @Value("${naver.client-secret}") String clientSecret,
            RestClient.Builder builder
    ) {
        this.restClient = builder.baseUrl(NAVER_URL)
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(new NaverBookErrorHandler())
                .build();
    }

    @Override
    public ApiBookResponses searchBooks(Keyword keyword) {
        return ApiCallHandler.execute(() -> restClient.get()
                .uri(uriBuilder -> uriBuilder.path(SEARCH_URI)
                        .queryParam("query", keyword.getKeyword())
                        .build())
                .retrieve()
                .body(ApiBookResponses.class), "[ERROR] 네이버 API 통신 중 문제 발생");
    }
}
