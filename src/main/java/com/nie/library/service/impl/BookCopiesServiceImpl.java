package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCopies;
import com.nie.library.entity.Books;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.mapper.BooksMapper;
import com.nie.library.service.IBookCopiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图书副本表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class BookCopiesServiceImpl extends ServiceImpl<BookCopiesMapper, BookCopies> implements IBookCopiesService {

    @Autowired
    private BookCopiesMapper bookCopiesMapper;

    @Override
    public ResultVO getBookCopy(PaginationForm paginationForm) {  // 获取所有书籍副本列表
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        Page<BookCopies> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<BookCopies> bookCopyPage = bookCopiesMapper.selectPage(page, queryWrapper);
        List<BookCopies> list = bookCopyPage.getRecords();
        resultVO.setData(list);
        resultVO.setCode(0);
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(bookCopyPage.getCurrent());
        paginationVO.setPageSize(bookCopyPage.getSize());
        paginationVO.setTotal(bookCopyPage.getTotal());
        resultVO.setPaginationVO(paginationVO);
        resultVO.setMsg("成功获取书籍副本信息");
        return resultVO;
    }

    @Override
    public ResultVO searchBookCopy(SearchForm searchForm, PaginationForm paginationForm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("书名")){
                    queryWrapper.eq(BookCopies::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.eq(BookCopies::getBookId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏ID")){
                    queryWrapper.eq(BookCopies::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏位置")){
                    queryWrapper.eq(BookCopies::getLocation, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏状态")){
                    queryWrapper.eq(BookCopies::getStatus, searchForm.getSearchContent());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("书名")){
                    queryWrapper.like(BookCopies::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.like(BookCopies::getBookId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏ID")){
                    queryWrapper.like(BookCopies::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏位置")){
                    queryWrapper.like(BookCopies::getLocation, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("馆藏状态")){
                    queryWrapper.like(BookCopies::getStatus, searchForm.getSearchContent());
                }
            }
            if(searchForm.getPublisherStartTime() != null && !searchForm.getPublisherStartTime().isEmpty()){
                LocalDate startDate = LocalDate.parse(searchForm.getPublisherStartTime(), formatter);
                System.out.println(searchForm.getPublisherStartTime());
                queryWrapper.ge(BookCopies::getPurchaseDate,startDate);
            }
            if(searchForm.getPublisherEndTime() != null && !searchForm.getPublisherEndTime().isEmpty()){
                LocalDate endDate = LocalDate.parse(searchForm.getPublisherEndTime(), formatter);
                System.out.println(searchForm.getPublisherEndTime());
                queryWrapper.le(BookCopies::getPurchaseDate,endDate);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<BookCopies> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<BookCopies> bookPageList = bookCopiesMapper.selectPage(page, queryWrapper);
        List<BookCopies> list = bookPageList.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(bookPageList.getCurrent());
        paginationVO.setPageSize(bookPageList.getSize());
        paginationVO.setTotal(bookPageList.getTotal());
        // 2.根据对应字段进行查询返回数据
        if (list != null && list.size() > 0) {
            resultVO.setData(list);
            resultVO.setCode(0);
            resultVO.setMsg("查询成功，查询到"+list.size()+"条结果");
            resultVO.setPaginationVO(paginationVO);
            return resultVO;
        }
        resultVO.setCode(-1);
        resultVO.setData(null);
        resultVO.setMsg("查询成功，没有查询结果");
        resultVO.setPaginationVO(paginationVO);
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectCopy(Integer id) {
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(BookCopies::getCopyId,id);
        BookCopies copies = bookCopiesMapper.selectOne(queryWrapper);
        int rows = bookCopiesMapper.delete(queryWrapper);
        if(rows > 0 && copies != null){
            resultVO.setCode(0);
            resultVO.setMsg("删除成功，删除"+rows+"条数");
            resultVO.setData(rows);
        }else if(copies == null){
            resultVO.setCode(-1);
            resultVO.setMsg("删除失败，没有找到对应数据");
            resultVO.setData(null);
        }
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectCopyList(List<Integer> ids) {
        LambdaQueryWrapper<BookCopies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BookCopies::getCopyId,ids);
        List<BookCopies> copyList = bookCopiesMapper.selectList(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if(copyList != null && copyList.size() > 0){
            int rows = bookCopiesMapper.delete(queryWrapper);
            if(rows > 0){
                resultVO.setCode(0);
                resultVO.setMsg("成功删除"+rows+"条书籍信息");
                resultVO.setData(rows);
            }else{
                resultVO.setCode(-2);
                resultVO.setMsg("删除失败，请检查后台");
            }
        }else{
            resultVO.setCode(-1);
            resultVO.setMsg("选中书籍不存在，请重新选择");
        }
        return resultVO;
    }
}
