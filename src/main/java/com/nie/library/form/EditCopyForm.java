package com.nie.library.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EditCopyForm {
    private Integer copyId;
    private Integer bookId;
    private String barcode;
    private String location;
    private String status;
    private LocalDate purchaseDate;
    private String notes;
}
