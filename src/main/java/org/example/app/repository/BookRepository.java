package org.example.app.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);

//    private final List<Book> repo = new ArrayList<>();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)",
                parameterSource);
        logger.info("store new book " + book);
    }

    @Override
    public boolean removeItemById(Integer bookIdRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        logger.info("remove book complete");
        return true;
    }

    @Override
    public boolean removeBookByAuthor(String author) {
        for (Book book : retreiveAll()) {
            if (book.getAuthor().equals(author)) {
                logger.info("remove books to author complete: " + book);
//                repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeBookByTitle(String title) {
        for (Book book : retreiveAll()) {
            if (book.getTitle().equals(title)) {
                logger.info("remove books to title complete: " + book);
//                repo.remove(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeBookBySize(Integer size) {
        for (Book book : retreiveAll()) {
            if (book.getSize().equals(size)) {
                logger.info("remove books to size complete: " + book);
//                repo.remove(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean filterBookByAuthor(String author) {
        retreiveAll().stream().filter(b -> b.getAuthor().equals(author))
                .collect(Collectors.toList());
        return true;
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
