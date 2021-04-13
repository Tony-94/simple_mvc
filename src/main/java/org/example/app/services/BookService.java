package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }


    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookId(Integer bookIdRemove) {
        return bookRepo.removeItemById(bookIdRemove);
    }

    public boolean removeBookByAuthor(String author) {
        return bookRepo.removeBookByAuthor(author);
    }

    public boolean removeBookByTitle(String title) {
        return bookRepo.removeBookByTitle(title);
    }

    public boolean removeBookBySize(Integer size) {
        return bookRepo.removeBookBySize(size);
    }

    public boolean filterBookByAuthor(String author) {
        return bookRepo.filterBookByAuthor(author);
    }

    public void filterBookByTitle(String title) {
        bookRepo.filterBookByTitle(title);
    }

    public void filterBookBySize(Integer size) {
        bookRepo.filterBookBySize(size);
    }
}
