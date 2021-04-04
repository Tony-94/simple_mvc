package org.example.app.services;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdRemove);

    boolean removeBookByAuthor(String author);

    boolean removeBookByTitle(String title);

    boolean removeBookBySize(Integer size);
}
