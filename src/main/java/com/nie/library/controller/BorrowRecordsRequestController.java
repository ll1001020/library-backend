package com.nie.library.controller;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Users;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.BorrowRecordsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 借阅记录表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-26
 */
@RestController
@RequestMapping("/library/borrow-records-request")
public class BorrowRecordsRequestController {
    @Autowired
    private BorrowRecordsRequestService iborrowRecordsRequestService;

    // 申请借阅书籍
    @PostMapping("/applyBorrow")
    public ResultVO applyBorrow(@RequestBody Users user,@RequestParam Integer id){
        ResultVO resultVO = this.iborrowRecordsRequestService.applyBorrow(user,id);
        return resultVO;
    }

    // 获取所有申请借阅记录
    @PostMapping("/getBorrowRequest")
    public ResultVO getBorrowRequest(@RequestBody PaginationForm paginationForm){
        ResultVO resultVO = this.iborrowRecordsRequestService.getBorrowRequest(paginationForm);
        return resultVO;
    }

    // 搜素申请借阅记录
    @PostMapping("/searchRequest")
    public ResultVO searchRequest(@RequestBody SearchForm searchForm ,PaginationForm paginationForm){
        ResultVO resultVO = this.iborrowRecordsRequestService.searchRequest(searchForm,paginationForm);
        return resultVO;
    }

    // 批准选中的借阅记录
    @PostMapping("/permitRequest")
    public ResultVO getPermitRequest(@RequestBody Users user , @RequestParam Integer id){
        ResultVO resultVO = this.iborrowRecordsRequestService.permitRequest(user,id);
        return resultVO;
    }

    // 打回选中的借阅记录
    @PostMapping("/permitRequestReject")
    public ResultVO permitRequestReject(@RequestBody Users user , @RequestParam Integer id){
        ResultVO resultVO = this.iborrowRecordsRequestService.permitRequestReject(user,id);
        return resultVO;
    }

    // 批量删除选中的借阅记录
    @PostMapping("/deleteSelectRequest")
    public ResultVO deleteSelectRequest(@RequestBody List<Integer> ids){
        ResultVO resultVO = this.iborrowRecordsRequestService.deleteSelectRequest(ids);
        return resultVO;
    }
}
