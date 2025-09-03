package finalmission.presentation;

import finalmission.application.BookService;
import finalmission.dto.request.BookCreateRequest;
import finalmission.dto.request.LoginUser;
import finalmission.dto.response.BookCreateResponse;
import finalmission.dto.response.BookResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<BookResponse>> searchBooks(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(required = false) String keyword
    ) {
        List<BookResponse> responses = bookService.searchBooks(keyword);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/admin/books")
    public ResponseEntity<BookCreateResponse> registerBook(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody @Valid BookCreateRequest request
    ) {
        BookCreateResponse response = bookService.registerBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
