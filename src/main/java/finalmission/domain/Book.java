package finalmission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Book {

    private final static int MIN_TOTAL_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String author;

    String image;

    String publisher;

    LocalDate pubdate;

    String isbn;

    String description;

    int totalCount;

    int availableCount;

    LocalDate regDate;

    public static Book createBook(String title, String author, String image, String publisher, LocalDate pubdate,
                                  String isbn, String description, int totalCount, LocalDate regDate) {
        validateTotalCount(totalCount);
        validateRegDate(regDate);
        return new Book(null, title, author, image, publisher, pubdate, isbn, description, totalCount, totalCount, regDate);
    }

    private static void validateTotalCount(int totalCount) {
        if (totalCount < MIN_TOTAL_COUNT) {
            throw new IllegalArgumentException("[ERROR] 등록할 책의 총 수량은 1 이상이어야 합니다.");
        }
    }

    private static void validateRegDate(LocalDate regDate) {
        if (regDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 등록일자는 현재시간 이후여야 합니다.");
        }
    }

    public void adjustAvailableCount(int count) {
        availableCount -= count;
    }
}
