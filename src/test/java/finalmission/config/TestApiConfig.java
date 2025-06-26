package finalmission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class TestApiConfig {

    private static final String NAVER_URL = "https://openapi.naver.com";

    @Bean
    public RestClient naverRestClient(
            RestClient.Builder builder
    ) {
        return builder.baseUrl(NAVER_URL)
                .defaultHeader("X-Naver-Client-Id", "test-client-id")
                .defaultHeader("X-Naver-Client-Secret", "test-client-secret")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
