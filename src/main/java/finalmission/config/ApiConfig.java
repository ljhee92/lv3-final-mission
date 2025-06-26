package finalmission.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@PropertySource("classpath:secure.properties")
public class ApiConfig {

    private static final String NAVER_URL = "https://openapi.naver.com";

    @Bean
    public RestClient naverRestClient(
            @Value("${naver.client-id}") String clientId,
            @Value("${naver.client-secret}") String clientSecret,
            RestClient.Builder builder
    ) {
        return builder.baseUrl(NAVER_URL)
                .requestFactory(createRequestFactory())
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private SimpleClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        return requestFactory;
    }
}
