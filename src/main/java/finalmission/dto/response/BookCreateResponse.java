package finalmission.dto.response;

import finalmission.domain.Book;

import java.time.LocalDate;

public record BookCreateResponse(
        Long id,

        BookResponse bookResponse,

        int totalCount,

        LocalDate regDate
) {

    public static BookCreateResponse from(Book book) {
        return new BookCreateResponse(
                book.getId(),
                BookResponse.from(book),
                book.getTotalCount(),
                book.getRegDate()
        );
    }
}
