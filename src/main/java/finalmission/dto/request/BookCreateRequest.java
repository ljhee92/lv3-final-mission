package finalmission.dto.request;

import java.time.LocalDate;

public record BookCreateRequest(
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
}
