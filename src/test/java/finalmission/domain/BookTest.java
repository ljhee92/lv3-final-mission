package finalmission.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookTest {

    @Test
    void 책의_총_수량이_1미만이면_예외가_발생한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 0;
        LocalDate regDate = LocalDate.now();

        assertThatThrownBy(() -> Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 책_등록일자가_현재시간이전이면_예외가_발생한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 책의_대여가능_수량을_조정한다() {
        String title = "오브젝트";
        String author = "조영호";
        String image = "https://shopping-phinf.pstatic.net/main_3245323/32453230352.20230627102640.jpg";
        String publisher = "위키북스";
        LocalDate pubdate = LocalDate.of(2019, 6, 17);
        String isbn = "9791158391409";
        String description = "오브젝트설명";
        int totalCount = 2;
        LocalDate regDate = LocalDate.now();

        Book book = Book.createBook(title, author, image, publisher, pubdate, isbn, description, totalCount, regDate);
        book.adjustAvailableCount(1);

        assertThat(book.getAvailableCount()).isEqualTo(1);
    }
}
