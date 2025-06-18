package finalmission.presentation;

import finalmission.application.BookService;
import finalmission.dto.request.LoginUser;
import finalmission.dto.response.BookSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/admin/books")
    public ResponseEntity<List<BookSearchResponse>> searchBooks(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(required = false) String keyword
    ) {
        List<BookSearchResponse> responses = bookService.searchBooks(keyword);
        return ResponseEntity.ok(responses);
    }
}
