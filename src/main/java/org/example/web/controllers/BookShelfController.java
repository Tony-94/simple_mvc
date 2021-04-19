package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.UploadFileExceptions;
import org.example.app.services.BookService;
import org.example.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;


@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);

    private static final String EXTERNAL_FILE_PATH = "/Users/antonzukov/Desktop/Java/apache-tomcat-9.0.37/external_uploads";

    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookId", new BookId());
        model.addAttribute("bookAuthor", new BookAuthor());
        model.addAttribute("bookTitle", new BookTitle());
        model.addAttribute("bookSize", new BookSize());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping(value = "/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookId", new BookId());
            model.addAttribute("bookAuthor", new BookAuthor());
            model.addAttribute("bookTitle", new BookTitle());
            model.addAttribute("bookSize", new BookSize());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/remove")
    public String removeBook(@Valid BookId bookId, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookAuthor", new BookAuthor());
            model.addAttribute("bookTitle", new BookTitle());
            model.addAttribute("bookSize", new BookSize());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookId(bookId.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/removeAuthor")
    public String removeBooks(@Valid BookAuthor bookAuthor, BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookId", new BookId());
            model.addAttribute("bookTitle", new BookTitle());
            model.addAttribute("bookSize", new BookSize());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookByAuthor(bookAuthor.getAuthor());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/removeTitle")
    public String removeBookToTitle(@Valid BookTitle bookTitle, BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookId", new BookId());
            model.addAttribute("bookAuthor", new BookAuthor());
            model.addAttribute("bookSize", new BookSize());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookByTitle(bookTitle.getTitle());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/removeSize")
    public String removeBookBySize(@Valid BookSize bookSize, BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookId", new BookId());
            model.addAttribute("bookAuthor", new BookAuthor());
            model.addAttribute("bookTitle", new BookTitle());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookBySize(bookSize.getSizeBook());
            return "redirect:/books/shelf";
        }
    }

    @GetMapping(value = "/filterByAuthor")
    public String filterBookByAuthor(@Valid BookAuthor bookAuthor, BindingResult bindingResult, Model model) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookId", new BookId());
            model.addAttribute("bookAuthor", new BookAuthor());
            model.addAttribute("bookTitle", new BookTitle());
            model.addAttribute("bookSize", new BookSize());
            model.addAttribute("bookList", bookService.filterBookByAuthor(bookAuthor.getAuthor()));
            return "book_shelf";
    }

    @GetMapping(value = "/filterByTitle")
    public String filterBookByTitle(@Valid BookTitle bookTitle, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookId", new BookId());
        model.addAttribute("bookAuthor", new BookAuthor());
        model.addAttribute("bookTitle", new BookTitle());
        model.addAttribute("bookSize", new BookSize());
        model.addAttribute("bookList", bookService.filterBookByTitle(bookTitle.getTitle()));
        return "book_shelf";
    }

    @GetMapping(value = "/filterBySize")
    public String filterBookBySize(@Valid BookSize bookSize, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookId", new BookId());
        model.addAttribute("bookAuthor", new BookAuthor());
        model.addAttribute("bookTitle", new BookTitle());
        model.addAttribute("bookSize", new BookSize());
        model.addAttribute("bookList", bookService.filterBookBySize(bookSize.getSizeBook()));
        return "book_shelf";
    }

    @PostMapping(value = "/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
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
        } else {
            logger.info("file is empty");
            throw new UploadFileExceptions("no file selected");
        }
    }

    @GetMapping("/file/{fileName:.+}")
    public void downloadFile(HttpServletResponse response,
                                    @PathVariable("fileName") String fileName) throws IOException {

        File file = new File(EXTERNAL_FILE_PATH + fileName);
        if (file.exists()) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);

            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

            //Here we have mentioned it to show as attachment
            // response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }
    }

    @ExceptionHandler(UploadFileExceptions.class)
    public String handleError(Model model, UploadFileExceptions exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/500";
    }
}
