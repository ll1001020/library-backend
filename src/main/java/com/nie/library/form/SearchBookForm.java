package com.nie.library.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchBookForm {
    private String searchContent;
    private String searchType;
    private Integer searchPrecise;
    private String publisherStartTime;
    private String publisherEndTime;
}
