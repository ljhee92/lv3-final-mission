package finalmission.infrastructure.thirdparty;

import finalmission.exception.ApiException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class GithubErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(
            URI url,
            HttpMethod method,
            ClientHttpResponse response
    ) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();

        if (statusCode.isSameCodeAs(HttpStatus.UNAUTHORIZED)
            || statusCode.isSameCodeAs(HttpStatus.FORBIDDEN)) {
            throw new ApiException(response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (statusCode.is5xxServerError()) {
            throw new ApiException(response.getStatusText(), statusCode);
        }
    }
}
