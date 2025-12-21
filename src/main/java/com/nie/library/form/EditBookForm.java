package com.nie.library.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EditBookForm {
    private Integer bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private double price;
    private String summary;
}
