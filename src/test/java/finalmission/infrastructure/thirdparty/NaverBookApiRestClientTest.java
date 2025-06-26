package finalmission.infrastructure.thirdparty;

import finalmission.config.TestApiConfig;
import finalmission.domain.Keyword;
import finalmission.dto.response.ApiBookResponses;
import finalmission.exception.NaverApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(NaverBookApiRestClient.class)
@Import(TestApiConfig.class)
class NaverBookApiRestClientTest {

    @Autowired
    private NaverBookApiRestClient naverBookApiRestClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void 도서_검색결과를_반환한다() {
        String keyword = "오브젝트";
        String encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String responseBody = "{\"items\":[]}";

        server.expect(once(), requestTo(startsWith("https://openapi.naver.com/v1/search/book.json")))
                .andExpect(queryParam("query", encodedKeyWord))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andExpect(header("X-Naver-Client-Id", "test-client-id"))
                .andExpect(header("X-Naver-Client-Secret", "test-client-secret"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        ApiBookResponses result = naverBookApiRestClient.searchBooks(Keyword.from(keyword));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.items()).isEmpty()
        );
    }

    @Test
    void 네이버_클라이언트_정보가_잘못되면_예외가_발생한다() {
        String keyword = "오브젝트";
        String encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        server.expect(once(), requestTo(startsWith("https://openapi.naver.com/v1/search/book.json")))
                .andExpect(queryParam("query", encodedKeyWord))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> naverBookApiRestClient.searchBooks(Keyword.from(keyword)))
                .isInstanceOf(NaverApiException.class)
                .satisfies(e -> {
                    NaverApiException ex = (NaverApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void 서버에러_발생시_예외가_발생한다() {
        String keyword = "오브젝트";
        String encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        server.expect(once(), requestTo(startsWith("https://openapi.naver.com/v1/search/book.json")))
                .andExpect(queryParam("query", encodedKeyWord))
                .andRespond(withServerError());

        assertThatThrownBy(() -> naverBookApiRestClient.searchBooks(Keyword.from(keyword)))
                .isInstanceOf(NaverApiException.class)
                .satisfies(e -> {
                    NaverApiException ex = (NaverApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Test
    void API_통신중_에러가_발생하면_예외가_발생한다() {
        String keyword = "오브젝트";
        String encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        server.expect(once(), requestTo(startsWith("https://openapi.naver.com/v1/search/book.json")))
                .andExpect(queryParam("query", encodedKeyWord))
                .andRespond(request -> {
                    throw new RestClientException("API 오류");
                });

        assertThatThrownBy(() -> naverBookApiRestClient.searchBooks(Keyword.from(keyword)))
                .isInstanceOf(NaverApiException.class)
                .satisfies(e -> {
                    NaverApiException ex = (NaverApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                });
    }

    @Test
    void API_통신중_연결이나_읽기가_지연되면_예외가_발생한다() {
        String keyword = "오브젝트";
        String encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        server.expect(once(), requestTo(startsWith("https://openapi.naver.com/v1/search/book.json")))
                .andExpect(queryParam("query", encodedKeyWord))
                .andRespond(request -> {
                    throw new ResourceAccessException("time out");
                });

        assertThatThrownBy(() -> naverBookApiRestClient.searchBooks(Keyword.from(keyword)))
                .isInstanceOf(NaverApiException.class)
                .satisfies(e -> {
                    NaverApiException ex = (NaverApiException) e;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
                });
    }
} 