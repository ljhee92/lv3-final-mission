package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Keyword;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.response.BookCreateResponse;
import finalmission.dto.response.BookResponse;
import finalmission.dto.response.NaverBookResponses;
import finalmission.infrastructure.thirdparty.ApiRestClient;
import finalmission.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    private final ApiRestClient apiRestClient;
    private final BookRepository bookRepository;

    public BookService(
            ApiRestClient apiRestClient,
            BookRepository bookRepository
    ) {
        this.apiRestClient = apiRestClient;
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> searchBooks(String keyword) {
        NaverBookResponses naverBookResponses = apiRestClient.searchBooks(Keyword.from(keyword));
        return naverBookResponses.items()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional
    public BookCreateResponse registerBook(BookCreateRequest request) {
        Book bookWithoutId = createBook(request);
        Book bookWithId = bookRepository.save(bookWithoutId);
        return BookCreateResponse.from(bookWithId);
    }

    private Book createBook(BookCreateRequest request) {
        return Book.createBook(
                request.title(),
                request.author(),
                request.image(),
                request.publisher(),
                request.pubdate(),
                request.isbn(),
                request.description(),
                request.totalCount(),
                request.regDate()
        );
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 도서입니다."));
    }
}
