package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);

    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            book.setId(book.hashCode());
            logger.info("store new book " + book);
            repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdRemove)) {
                logger.info("remove book complete: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeBookByAuthor(String author) {
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(author)) {
                logger.info("remove books to author complete: " + book);
                repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeBookByTitle(String title) {
        for (Book book : retreiveAll()) {
            if (book.getTitle().equals(title)) {
                logger.info("remove books to title complete: " + book);
                repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeBookBySize(Integer size) {
        for (Book book : retreiveAll()) {
            if (book.getSize().equals(size)) {
                logger.info("remove books to size complete: " + book);
                repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean filterBookByAuthor(String author) {
        retreiveAll().stream().filter(b -> b.getAuthor().equals(author))
                .collect(Collectors.toList());
        return false;
    }

    @Override
    public void filterBookByTitle(String title) {
        retreiveAll().stream().filter(b -> b.getTitle().equals(title))
                .collect(Collectors.toList());
    }

    @Override
    public void filterBookBySize(Integer size) {
        retreiveAll().stream().filter(b -> b.getSize() >= size)
                .collect(Collectors.toList());
    }
}
