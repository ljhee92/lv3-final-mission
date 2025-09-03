package finalmission.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/my-reservations")
    public String myReservations() {
        return "my-reservations";
    }

    @GetMapping("/admin/book")
    public String adminBook() {
        return "admin/books";
    }
}
