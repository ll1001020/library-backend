package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.IBookCopiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 图书副本表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@RestController
@RequestMapping("/library/book-copies")
public class BookCopiesController {
    @Autowired
    private IBookCopiesService ibookCopiesService;

    // 获取所有书籍副本列表
    @PostMapping("/getBookCopy")
    public ResultVO getBookCopy(@RequestBody PaginationForm paginationForm){
        ResultVO resultVO = this.ibookCopiesService.getBookCopy(paginationForm);
        return resultVO;
    }

    // 根据条件查询书籍副本
    @PostMapping("/searchBookCopy")
    public ResultVO searchBookCopy(@RequestBody SearchForm searchForm, PaginationForm paginationForm){
        ResultVO resultVO = this.ibookCopiesService.searchBookCopy(searchForm,paginationForm);
        return resultVO;
    }


    // 删除选中书籍副本
    @GetMapping("/deleteSelectCopy")
    public ResultVO deleteSelectCopy(Integer id){
        ResultVO resultVO = this.ibookCopiesService.deleteSelectCopy(id);
        return resultVO;
    }

    // 批量删除书籍副本
    @PostMapping("/deleteSelectCopyList")
    public ResultVO deleteSelectCopyList(@RequestBody List<Integer> ids){
        ResultVO resultVO = this.ibookCopiesService.deleteSelectCopyList(ids);
        return resultVO;
    }

}

