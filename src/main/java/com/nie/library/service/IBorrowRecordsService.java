package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BorrowRecords;
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
 * @since 2025-12-11
 */
public interface IBorrowRecordsService extends IService<BorrowRecords> {

    // 获取所有借阅记录
    public ResultVO getBorrowRecord(PaginationForm paginationForm);

    // 搜索借阅记录
    public ResultVO searchRecord(SearchForm searchForm, PaginationForm paginationForm);

    // 登记选中借阅记录归还
    public ResultVO borrowReturn(Users user, Integer id);

    // 为选中记录续借一次
    public ResultVO borrowRenew(Users user, Integer id);

    // 批量删除选中的借阅记录
    public ResultVO deleteSelectBorrowList(List<Integer> ids);
}
