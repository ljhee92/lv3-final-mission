package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken
) {

    public static TokenResponse from(String accessToken) {
        return new TokenResponse(accessToken);
    }
}
