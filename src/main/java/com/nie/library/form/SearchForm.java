package com.nie.library.form;

import lombok.Data;

@Data
public class SearchForm {
    private String searchContent;
    private String searchType;
    private Integer searchPrecise;
    private String publisherStartTime;
    private String publisherEndTime;
}
