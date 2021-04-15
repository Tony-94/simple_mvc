package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


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
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping(value = "/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repositore size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookId(bookIdToRemove.getId());
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

    @PostMapping(value = "/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file)  throws Exception {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (dir.exists()) {
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";

    }
}
