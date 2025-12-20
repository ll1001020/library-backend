package com.nie.library.form;

import lombok.Data;

@Data
public class PaginationForm {
    private Integer currentPage; // 当前是第几页
    private Integer pageSize; // 一页有多少条
}
