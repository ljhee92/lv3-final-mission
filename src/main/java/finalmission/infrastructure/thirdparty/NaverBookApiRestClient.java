package finalmission.infrastructure.thirdparty;

import finalmission.domain.Keyword;
import finalmission.dto.response.ApiBookResponses;
import finalmission.exception.NaverApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class NaverBookApiRestClient implements BookApiRestClient {

    private static final String SEARCH_URI = "/v1/search/book.json";

    private final RestClient restClient;

    public NaverBookApiRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ApiBookResponses searchBooks(Keyword keyword) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path(SEARCH_URI)
                            .queryParam("query", keyword.getKeyword())
                            .build())
                    .retrieve()
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.UNAUTHORIZED), (req, res) -> {
                        throw new NaverApiException(res.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new NaverApiException(res.getStatusText(), res.getStatusCode());
                    })
                    .body(ApiBookResponses.class);
        } catch (RestClientException e) {
            throw new NaverApiException("[ERROR] API 통신 중 문제 발생", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
