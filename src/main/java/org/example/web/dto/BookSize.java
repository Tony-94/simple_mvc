package org.example.web.dto;

import javax.validation.constraints.NotNull;

public class BookSize {

    @NotNull
    private Integer sizeBook;


    public Integer getSizeBook() {
        return sizeBook;
    }

    public void setSizeBook(Integer sizeBook) {
        this.sizeBook = sizeBook;
    }
}
