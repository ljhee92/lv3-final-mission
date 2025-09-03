package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalmission.domain.Book;

import java.time.LocalDate;

public record BookResponse(
        String title,

        String author,

        String image,

        String publisher,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate pubdate,

        String isbn,

        String description
) {

    public static BookResponse from(ApiBookResponse response) {
        return new BookResponse(
                response.title(),
                response.author(),
                response.image(),
                response.publisher(),
                response.pubdate(),
                response.isbn(),
                response.description()
        );
    }

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.getPublisher(),
                book.getPubdate(),
                book.getIsbn(),
                book.getDescription()
        );
    }
}
