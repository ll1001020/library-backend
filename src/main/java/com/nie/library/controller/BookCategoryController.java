package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.IBookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-17
 */
@RestController
@RequestMapping("/library/book-category")
public class BookCategoryController {
    @Autowired
    private IBookCategoryService ibookCategoryService;
    @GetMapping("/getBookCategoryTree")
    public ResultVO getBookCategoryTree(){  // 获取书籍分类树
        ResultVO resultVO = ibookCategoryService.getBookCategoryTree();
        return resultVO;
    }

}

