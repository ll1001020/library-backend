package com.nie.library.VO;

import lombok.Data;

@Data
public class PaginationVO {
    private Long currentPage;
    private Long pageSize;
    private Long total;
}
