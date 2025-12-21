package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCopies;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;

import java.util.List;

/**
 * <p>
 * 图书副本表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IBookCopiesService extends IService<BookCopies> {
    // 获取所有书籍副本列表
    public ResultVO getBookCopy(PaginationForm paginationForm);

    // 搜索书籍副本列表
    public ResultVO searchBookCopy(SearchForm searchForm, PaginationForm paginationForm);

    // 删除选中书籍副本
    public ResultVO deleteSelectCopy(Integer id);

    // 批量删除
    public ResultVO deleteSelectCopyList(List<Integer> ids);
}
