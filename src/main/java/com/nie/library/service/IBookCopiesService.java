package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCopies;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.AddCopyForm;
import com.nie.library.form.EditCopyForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import org.springframework.web.multipart.MultipartFile;

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

    // 修改选中书籍副本
    public ResultVO editSelectCopy(EditCopyForm editCopyForm);

    // 新增书籍副本
    public ResultVO addCopy(AddCopyForm addCopyForm);

    // 批量新增图书副本
    public ResultVO addBatchCopy(MultipartFile file);

    // 根据ID获取对应书籍可借副本信息，只取第一条
    public ResultVO getBookCopyById(Integer id);
}
