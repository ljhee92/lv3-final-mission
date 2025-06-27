package finalmission.infrastructure.thirdparty;

import finalmission.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

public class ApiCallHandler {

    public static <T> T execute(RestCall<T> call, String message) {
        try {
            return call.call();
        } catch (RestClientException e) {
            throw new ApiException(message, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @FunctionalInterface
    public interface RestCall<T> {
        T call();
    }
}
