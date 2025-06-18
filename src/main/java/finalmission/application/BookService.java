package finalmission.application;

import finalmission.domain.Keyword;
import finalmission.dto.response.BookSearchResponse;
import finalmission.dto.response.NaverBookResponses;
import finalmission.infrastructure.thirdparty.ApiRestClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ApiRestClient apiRestClient;

    public BookService(ApiRestClient apiRestClient) {
        this.apiRestClient = apiRestClient;
    }

    public List<BookSearchResponse> searchBooks(String keyword) {
        NaverBookResponses naverBookResponses = apiRestClient.searchBooks(Keyword.from(keyword));
        return naverBookResponses.items()
                .stream()
                .map(BookSearchResponse::from)
                .toList();
    }
}
