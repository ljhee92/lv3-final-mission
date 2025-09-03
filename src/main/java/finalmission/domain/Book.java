package finalmission.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    private final static int MIN_TOTAL_COUNT = 1;
    private final static int MIN_AVAILABLE_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String image;

    private String publisher;

    private LocalDate pubdate;

    private String isbn;

    @Size(max = 1000)
    private String description;

    private int totalCount;

    private int availableCount;

    private LocalDate regDate;

    private Book(String title, String author, String image, String publisher, LocalDate pubdate,
                 String isbn, String description, int totalCount, LocalDate regDate) {
        validateTotalCount(totalCount);
        validateRegDate(regDate);
        this.title = title;
        this.author = author;
        this.image = image;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.isbn = isbn;
        this.description = description;
        this.totalCount = totalCount;
        this.availableCount = totalCount;
        this.regDate = regDate;
    }

    public static Book createBook(String title, String author, String image, String publisher, LocalDate pubdate,
                                  String isbn, String description, int totalCount, LocalDate regDate) {
        return new Book(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);
    }

    private void validateTotalCount(int totalCount) {
        if (totalCount < MIN_TOTAL_COUNT) {
            throw new IllegalArgumentException("[ERROR] 등록할 책의 총 수량은 1 이상이어야 합니다.");
        }
    }

    private void validateRegDate(LocalDate regDate) {
        if (regDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 등록일자는 현재시간 이후여야 합니다.");
        }
    }

    public void adjustAvailableCount(int count) {
        availableCount -= count;
    }

    public void checkAvailableCount() {
        if (this.availableCount < MIN_AVAILABLE_COUNT) {
            throw new IllegalArgumentException("[ERROR] 해당 도서의 예약 가능 수량이 부족합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        if (id != null && book.id != null) {
            return Objects.equals(id, book.id);
        }
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hashCode(id) : Objects.hashCode(isbn);
    }
}
