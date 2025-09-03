package finalmission.dto.response;

import finalmission.domain.Book;

public record AvailableBookResponse(
        Long bookId,

        BookResponse bookResponse,

        int availableCount,

        int totalCount
) {

    public static AvailableBookResponse from(Book book) {
        return new AvailableBookResponse(
                book.getId(),
                BookResponse.from(book),
                book.getAvailableCount(),
                book.getTotalCount()
        );
    }
}
