package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Books;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchBookForm;
import com.nie.library.mapper.BooksMapper;
import com.nie.library.service.IBooksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 图书基本信息表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements IBooksService {
    @Autowired
    private BooksMapper booksMapper;

    @Override
    public ResultVO getBookList(PaginationForm paginationForm) {
        QueryWrapper<Books> queryWrapper = new QueryWrapper<>();
        Page<Books> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<Books> bookPageList = booksMapper.selectPage(page, queryWrapper);
        List<Books> list = bookPageList.getRecords();
        ResultVO resultVO = new ResultVO();
        resultVO.setData(list);
        resultVO.setCode(0);
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(bookPageList.getCurrent());
        paginationVO.setPageSize(bookPageList.getSize());
        paginationVO.setTotal(bookPageList.getTotal());
        resultVO.setPaginationVO(paginationVO);
        resultVO.setMsg("成功获取书籍信息");
        return resultVO;
    }

    @Override
    public ResultVO searchBook(SearchBookForm searchBookForm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ResultVO resultVO = new ResultVO();
        QueryWrapper<Books> queryWrapper = new QueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchBookForm.getSearchType() != null){
            if(searchBookForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchBookForm.getSearchType().equals("书名")){
                    queryWrapper.eq("title",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("ISBN")){
                    queryWrapper.eq("isbn",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("作者")){
                    queryWrapper.eq("author",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("出版社")){
                    queryWrapper.eq("publisher",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("书籍ID")){
                    queryWrapper.eq("book_id",searchBookForm.getSearchContent());
                }
            }else if(searchBookForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchBookForm.getSearchType().equals("书名")){
                    queryWrapper.like("title",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("ISBN")){
                    queryWrapper.like("isbn",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("作者")){
                    queryWrapper.like("author",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("出版社")){
                    queryWrapper.like("publisher",searchBookForm.getSearchContent());
                }else if(searchBookForm.getSearchType().equals("书籍ID")){
                    queryWrapper.like("book_id",searchBookForm.getSearchContent());
                }
            }
            if(searchBookForm.getPublisherStartTime() != null && !searchBookForm.getPublisherStartTime().isEmpty()){
                LocalDate startDate = LocalDate.parse(searchBookForm.getPublisherStartTime(), formatter);
                System.out.println(searchBookForm.getPublisherStartTime());
                queryWrapper.ge("publish_date",startDate);
            }
            if(searchBookForm.getPublisherEndTime() != null && !searchBookForm.getPublisherEndTime().isEmpty()){
                LocalDate endDate = LocalDate.parse(searchBookForm.getPublisherEndTime(), formatter);
                System.out.println(searchBookForm.getPublisherEndTime());
                queryWrapper.le("publish_date",endDate);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("缺少查询类型");
            return resultVO;
        }

        List<Books> list = booksMapper.selectList(queryWrapper);
        // 2.根据对应字段进行查询返回数据
        if (list != null && list.size() > 0) {
            resultVO.setData(list);
            resultVO.setCode(0);
            resultVO.setMsg("查询成功，查询到"+list.size()+"条结果");
            return resultVO;
        }
        resultVO.setCode(-1);
        resultVO.setData(null);
        resultVO.setMsg("查询成功，没有查询结果");
        return resultVO;
    }
}
