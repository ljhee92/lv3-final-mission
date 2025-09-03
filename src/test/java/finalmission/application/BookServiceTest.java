package finalmission.application;

import finalmission.domain.Book;
import finalmission.domain.Keyword;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.response.ApiBookResponse;
import finalmission.dto.response.ApiBookResponses;
import finalmission.dto.response.BookCreateResponse;
import finalmission.dto.response.BookResponse;
import finalmission.infrastructure.thirdparty.BookApiRestClient;
import finalmission.repository.BookRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookServiceTest {

    @Mock
    private BookApiRestClient bookApiRestClient;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void 키워드로_책을_검색한다() {
        String keyword = "오브젝트";

        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        ApiBookResponse response = new ApiBookResponse(title, author, image, publisher, pubdate, isbn, description);
        ApiBookResponses responses = new ApiBookResponses(List.of(response));

        when(bookApiRestClient.searchBooks(Keyword.from(keyword)))
                .thenReturn(responses);

        BookResponse result = BookResponse.from(response);

        assertThat(bookService.searchBooks(keyword)).isEqualTo(List.of(result));
    }

    @Test
    void 책을_등록한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        BookCreateRequest request = new BookCreateRequest(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        when(bookRepository.save(book))
                .thenReturn(book);

        BookCreateResponse result = BookCreateResponse.from(book);

        assertThat(bookService.registerBook(request)).isEqualTo(result );
    }

    @Test
    void 등록되어있는_모든_책을_조회한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);
        List<Book> books = List.of(book);

        when(bookRepository.findAll())
                .thenReturn(books);

        assertThat(bookService.findAll()).isEqualTo(books);
    }

    @Test
    void 아이디로_책을_조회한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();
        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);

        when(bookRepository.findById(any()))
                .thenReturn(Optional.of(book));

        Long existsId = 1L;

        assertThat(bookService.findById(existsId)).isEqualTo(book);
    }

    @Test
    void 존재하지_않는_아이디로_책을_조회하면_예외가_발생한다() {
        when(bookRepository.findById(any()))
                .thenReturn(Optional.empty());

        Long notExistsId = 1000L;

        assertThatThrownBy(() -> bookService.findById(notExistsId))
                .isInstanceOf(NoSuchElementException.class);
    }
}
