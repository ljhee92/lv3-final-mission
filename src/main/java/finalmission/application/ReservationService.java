package finalmission.application;

import finalmission.domain.Book;
import finalmission.dto.response.AvailableBookResponse;
import finalmission.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final BookRepository bookRepository;

    public ReservationService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<AvailableBookResponse> getAvailableBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(AvailableBookResponse::from)
                .toList();
    }
}
