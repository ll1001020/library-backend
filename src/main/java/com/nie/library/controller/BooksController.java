package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchBookForm;
import com.nie.library.service.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 图书基本信息表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@RestController
@RequestMapping("/library/books")
public class BooksController {
    @Autowired
    private IBooksService ibooksService;

    @PostMapping("/getBooksList")
    public ResultVO getBooksList(PaginationForm paginationForm) {
        ResultVO resultVO = this.ibooksService.getBookList(paginationForm);
        return resultVO;
    }

    @PostMapping("/searchBook")
    public ResultVO searchBook(SearchBookForm searchBookForm){
        ResultVO resultVO = this.ibooksService.searchBook(searchBookForm);
        return resultVO;
    }

}

