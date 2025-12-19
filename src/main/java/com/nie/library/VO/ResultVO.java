package com.nie.library.VO;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;
    private T data;
    private String msg;
    private PaginationVO paginationVO;
}
