package finalmission.infrastructure.thirdparty;

import finalmission.domain.Keyword;
import finalmission.dto.response.ApiBookResponses;

public interface BookApiRestClient {

    ApiBookResponses searchBooks(Keyword keyword);
}
