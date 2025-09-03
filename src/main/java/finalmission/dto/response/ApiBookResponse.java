package finalmission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ApiBookResponse(
        String title,

        String author,

        String image,

        String publisher,

        @JsonFormat(pattern = "yyyyMMdd")
        LocalDate pubdate,

        String isbn,

        String description
) {
}
