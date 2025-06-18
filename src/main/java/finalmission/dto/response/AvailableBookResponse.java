package finalmission.dto.response;

import finalmission.domain.Book;

import java.time.LocalDate;

public record AvailableBookResponse(
        Long bookId,

        String title,

        String author,

        String image,

        String publisher,

        LocalDate pubdate,

        String isbn,

        String description,

        int availableCount,

        int totalCount
) {

    public static AvailableBookResponse from(Book book) {
        return new AvailableBookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.getPublisher(),
                book.getPubdate(),
                book.getIsbn(),
                book.getDescription(),
                book.getAvailableCount(),
                book.getTotalCount()
        );
    }
}
