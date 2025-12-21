package com.nie.library.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddBookForm {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishDate;
    private double price;
    private String summary;
    private String categoryName;
    private Integer categoryId;
    private String coverImage;
}
