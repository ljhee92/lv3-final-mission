package finalmission.fixture;

import finalmission.domain.Book;
import finalmission.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookFixture {

    private final BookRepository bookRepository;

    public BookFixture(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook1() {
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

        return bookRepository.save(book);
    }
}
