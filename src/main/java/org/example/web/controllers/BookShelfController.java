package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);

    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping(value = "/save")
    public String saveBook(Book book) {
            bookService.saveBook(book);
            logger.info("current repositore size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
    }

    @PostMapping(value = "/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdRemove) {
        if (bookService.removeBookId(bookIdRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/removeAuthor")
    public String removeBooks(@RequestParam(value = "author") String author) {
        bookService.removeBookByAuthor(author);
        return "redirect:/books/shelf";
    }

    @PostMapping(value = "/removeTitle")
    public String removeBookToTitle(@RequestParam(value = "title") String title) {
        bookService.removeBookByTitle(title);
        return "redirect:/books/shelf";
    }

    @PostMapping(value = "removeSize")
    public String removeBookBySize(@RequestParam(value = "size") Integer size) {
        bookService.removeBookBySize(size);
        return "redirect:/books/shelf";
    }

    @GetMapping(value = "/filterByAuthor")
    public String filterBookByAuthor(@RequestParam(value = "author") String author) {
        bookService.filterBookByAuthor(author);
        return "redirect:/books/shelf";
    }

    @GetMapping(value = "/filterByTitle")
    public String filterBookByTitle(@RequestParam(value = "title") String title) {
        bookService.filterBookByTitle(title);
        return "redirect:/books/shelf";
    }

    @GetMapping(value = "/filterBySize")
    public String filterBookBySize(@RequestParam(value = "size") Integer size) {
        bookService.filterBookBySize(size);
        return "redirect:/books/shelf";
    }
}
