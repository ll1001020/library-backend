package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BorrowRecordsRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.entity.Users;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;

import java.util.List;

/**
 * <p>
 * 借阅记录表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-26
 */
public interface BorrowRecordsRequestService extends IService<BorrowRecordsRequest> {

    // 申请借阅书籍
    public ResultVO applyBorrow(Users user, Integer id);

    // 获取所有申请借阅记录
    public ResultVO getBorrowRequest(PaginationForm paginationForm);

    // 搜素申请借阅记录
    public ResultVO searchRequest(SearchForm searchForm, PaginationForm paginationForm);

    // 批准选中的借阅申请
    public ResultVO permitRequest(Users user,Integer id);

    // 打回选中的借阅记录
    public ResultVO permitRequestReject(Users user, Integer id);

    // 批量删除选中的借阅记录
    public ResultVO deleteSelectRequest(List<Integer> ids);

}
