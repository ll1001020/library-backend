package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Books;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchBookForm;

/**
 * <p>
 * 图书基本信息表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IBooksService extends IService<Books> {
    public ResultVO getBookList(PaginationForm paginationForm);
    public ResultVO searchBook(SearchBookForm searchBookForm);
}
