package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.AddBookForm;
import com.nie.library.form.EditBookForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/getAllBooksList")
    public ResultVO getAllBooksList() {
        ResultVO resultVO = this.ibooksService.getAllBooksList();
        return resultVO;
    }

    @PostMapping("/getBooksList")
    public ResultVO getBooksList(PaginationForm paginationForm) {  // 获取书籍列表
        ResultVO resultVO = this.ibooksService.getBookList(paginationForm);
        return resultVO;
    }

    @PostMapping("/searchBook")
    public ResultVO searchBook(@RequestBody SearchForm searchForm, PaginationForm paginationForm) {  // 根据需求查询
        ResultVO resultVO = this.ibooksService.searchBook(searchForm,paginationForm);
        return resultVO;
    }

    @GetMapping("/deleteSelectBook")
    public ResultVO deleteSelectBook(Integer id){  // 删除选中书籍
        ResultVO resultVO = this.ibooksService.deleteSelectBook(id);
        return resultVO;
    }

    @PostMapping("/deleteSelectBookList")
    public ResultVO deleteSelectBookList(@RequestBody List<Integer> ids){  // 批量删除书籍
        ResultVO resultVO = this.ibooksService.deleteSelectBookList(ids);
        return resultVO;
    }

    @PostMapping("/editSelectBook")
    public ResultVO editSelectBook(@RequestBody EditBookForm editBookForm){  // 修改选中书籍
        ResultVO resultVO = this.ibooksService.editSelectBook(editBookForm);
        return resultVO;
    }

    @PostMapping("/addBook")
    public ResultVO addBook(@RequestBody AddBookForm addBookForm){  // 新增书籍
        ResultVO resultVO = this.ibooksService.addBook(addBookForm);
        return resultVO;
    }

    @PostMapping("/addBatchBook")
    public ResultVO addBatchBook(@RequestParam("file") MultipartFile file){  // 批量录入书籍
        ResultVO resultVO = this.ibooksService.addBatchBook(file);
        return resultVO;
    }

    @GetMapping("/findBookByCategoryId")
    public ResultVO findBookByCategoryId(@RequestParam Integer currentId){  // 通过分类ID查询书籍
        ResultVO resultVO = this.ibooksService.findBookByCategoryId(currentId);
        return resultVO;
    }
}

