package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Keyword;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.response.BookCreateResponse;
import finalmission.dto.response.BookResponse;
import finalmission.dto.response.ApiBookResponses;
import finalmission.infrastructure.thirdparty.BookApiRestClient;
import finalmission.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    private final BookApiRestClient bookApiRestClient;
    private final BookRepository bookRepository;

    public BookService(
            BookApiRestClient bookApiRestClient,
            BookRepository bookRepository
    ) {
        this.bookApiRestClient = bookApiRestClient;
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> searchBooks(String keyword) {
        ApiBookResponses apiBookResponses = bookApiRestClient.searchBooks(Keyword.from(keyword));
        return apiBookResponses.items()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    @Transactional
    public BookCreateResponse registerBook(BookCreateRequest request) {
        Book bookWithoutId = Book.createBook(
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
        Book bookWithId = bookRepository.save(bookWithoutId);
        return BookCreateResponse.from(bookWithId);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 도서입니다."));
    }
}
