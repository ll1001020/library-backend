package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.AddCopyForm;
import com.nie.library.form.EditCopyForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.IBookCopiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 编辑选中书籍信息
    @PostMapping("/editSelectCopy")
    public ResultVO editSelectCopy(@RequestBody EditCopyForm editCopyForm){
        ResultVO resultVO = this.ibookCopiesService.editSelectCopy(editCopyForm);
        return resultVO;
    }

    // 新增根据图书ID新增副本信息
    @PostMapping("/addCopy")
    public ResultVO addCopy(@RequestBody AddCopyForm addCopyForm){
        ResultVO resultVO = this.ibookCopiesService.addCopy(addCopyForm);
        return resultVO;
    }

    // 批量新增图书副本
    @PostMapping("/addBatchCopy")
    public ResultVO addBatchCopy(@RequestParam("file") MultipartFile file){
        ResultVO resultVO = this.ibookCopiesService.addBatchCopy(file);
        return resultVO;
    }

    // 根据ID获取对应书籍可借副本信息，只取第一条
    @GetMapping("/getBookCopyById")
    public ResultVO getBookCopyById(@RequestParam Integer id){
        ResultVO resultVO = this.ibookCopiesService.getBookCopyById(id);
        return resultVO;
    }
}

