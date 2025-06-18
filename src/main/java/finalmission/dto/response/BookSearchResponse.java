package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BookSearchResponse(
        String title,

        String author,

        String image,

        String publisher,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate pubdate,

        String isbn,

        String description
) {

    public static BookSearchResponse from(NaverBookResponse response) {
        return new BookSearchResponse(
                response.title(),
                response.author(),
                response.image(),
                response.publisher(),
                response.pubdate(),
                response.isbn(),
                response.description()
        );
    }
}
