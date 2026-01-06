package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Books;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.AddBookForm;
import com.nie.library.form.EditBookForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图书基本信息表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IBooksService extends IService<Books> {
    // 查询所有列表
    public ResultVO getAllBooksList();

    // 分页查询列表
    public ResultVO getBookList(PaginationForm paginationForm);

    // 条件查询
    public ResultVO searchBook(SearchForm searchForm, PaginationForm paginationForm);

    // 删除选中书籍
    public ResultVO deleteSelectBook(Integer id);

    // 批量删除
    public ResultVO deleteSelectBookList(List<Integer> ids);

    // 编辑选中书籍
    public ResultVO editSelectBook(EditBookForm editBookForm);

    // 新增书籍
    public ResultVO addBook(AddBookForm addBookForm);

    // 通过分类ID查询书籍
    public ResultVO findBookByCategoryId(Integer currentId);

    // 批量录入新书籍
    public ResultVO addBatchBook(MultipartFile file);

    // 获取本月最新的书籍
    public ResultVO getNewBookList();
}
