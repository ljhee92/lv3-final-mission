package finalmission.infrastructure.thirdparty;

import finalmission.domain.Keyword;
import finalmission.dto.response.NaverBookResponses;

public interface ApiRestClient {

    NaverBookResponses searchBooks(Keyword keyword);
}
