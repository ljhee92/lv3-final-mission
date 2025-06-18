package finalmission.dto.response;

import finalmission.domain.Book;

import java.time.LocalDate;

public record BookCreateResponse(
        Long id,

        String title,

        String author,

        String image,

        String publisher,

        LocalDate pubdate,

        String isbn,

        String description,

        int totalCount,

        LocalDate regDate
) {

    public static BookCreateResponse from(Book book) {
        return new BookCreateResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.getPublisher(),
                book.getPubdate(),
                book.getIsbn(),
                book.getDescription(),
                book.getTotalCount(),
                book.getRegDate()
        );
    }
}
