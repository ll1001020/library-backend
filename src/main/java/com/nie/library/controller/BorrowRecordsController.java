package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Users;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.BorrowRecordsMapper;
import com.nie.library.service.BorrowRecordsRequestService;
import com.nie.library.service.IBorrowRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 借阅记录表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@RestController
@RequestMapping("/library/borrow-records")
public class BorrowRecordsController {
    @Autowired
    private IBorrowRecordsService iborrowRecordsService;

    // 获取所有借阅记录
    @PostMapping("/getBorrowRecord")
    public ResultVO getBorrowRecord(@RequestBody PaginationForm paginationForm) {
        ResultVO resultVO = this.iborrowRecordsService.getBorrowRecord(paginationForm);
        return resultVO;
    }

    // 搜索借阅记录
    @PostMapping("/searchRecord")
    public ResultVO searchRecord(@RequestBody SearchForm searchForm,PaginationForm paginationForm) {
        ResultVO resultVO = this.iborrowRecordsService.searchRecord(searchForm,paginationForm);
        return resultVO;
    }

    // 登记选中借阅记录归还
    @PostMapping("/borrowReturn")
    public ResultVO borrowReturn(@RequestBody Users user, @RequestParam Integer id){
        ResultVO resultVO = this.iborrowRecordsService.borrowReturn(user,id);
        return resultVO;
    }

    // 为选中记录进行续借1次
    @PostMapping("/borrowRenew")
    public ResultVO borrowRenew(@RequestBody Users user, @RequestParam Integer id){
        ResultVO resultVO = this.iborrowRecordsService.borrowRenew(user,id);
        return resultVO;
    }

    // 批量删除选中的借阅记录
    @PostMapping("/deleteSelectBorrowList")
    public ResultVO deleteSelectBorrowList(@RequestBody List<Integer> ids){
        ResultVO resultVO = this.iborrowRecordsService.deleteSelectBorrowList(ids);
        return resultVO;
    }

}

