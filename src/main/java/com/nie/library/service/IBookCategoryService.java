package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-17
 */
public interface IBookCategoryService extends IService<BookCategory> {

    // 获取书籍分类树
    public ResultVO getBookCategoryTree();

}
