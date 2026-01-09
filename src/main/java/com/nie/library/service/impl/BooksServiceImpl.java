package com.nie.library.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Books;
import com.nie.library.form.*;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.mapper.BooksMapper;
import com.nie.library.service.IBooksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private BookCopiesMapper bookCopiesMapper;

    @Override
    public ResultVO getAllBooksList() {
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        List<Books> booksList = booksMapper.selectList(queryWrapper);
        ResultVO resultVO = new ResultVO();
        resultVO.setData(booksList);
        resultVO.setCode(0);
        resultVO.setMsg("成功获取所有书籍列表");
        return resultVO;
    }

    @Override
    public ResultVO getBookList(PaginationForm paginationForm) {  // 获取书籍列表
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
    public ResultVO searchBook(SearchForm searchForm, PaginationForm paginationForm) {  // 根据需求查询
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ResultVO resultVO = new ResultVO();
        QueryWrapper<Books> queryWrapper = new QueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("书名")){
                    queryWrapper.eq("title", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("ISBN")){
                    queryWrapper.eq("isbn", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("作者")){
                    queryWrapper.eq("author", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("出版社")){
                    queryWrapper.eq("publisher", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.eq("book_id", searchForm.getSearchContent());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("书名")){
                    queryWrapper.like("title", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("ISBN")){
                    queryWrapper.like("isbn", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("作者")){
                    queryWrapper.like("author", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("出版社")){
                    queryWrapper.like("publisher", searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.like("book_id", searchForm.getSearchContent());
                }
            }
            if(searchForm.getPublisherStartTime() != null && !searchForm.getPublisherStartTime().isEmpty()){
                LocalDate startDate = LocalDate.parse(searchForm.getPublisherStartTime(), formatter);
                System.out.println(searchForm.getPublisherStartTime());
                queryWrapper.ge("publish_date",startDate);
            }
            if(searchForm.getPublisherEndTime() != null && !searchForm.getPublisherEndTime().isEmpty()){
                LocalDate endDate = LocalDate.parse(searchForm.getPublisherEndTime(), formatter);
                System.out.println(searchForm.getPublisherEndTime());
                queryWrapper.le("publish_date",endDate);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<Books> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<Books> bookPageList = booksMapper.selectPage(page, queryWrapper);
        List<Books> list = bookPageList.getRecords();
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
    public ResultVO deleteSelectBook(Integer id) {  // 删除选中书籍
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(Books::getBookId,id);
        Books books = booksMapper.selectOne(queryWrapper);
        queryWrapper.eq(Books::getTotalCopies,0);
        int rows = booksMapper.delete(queryWrapper);
        if(rows > 0 && books != null){
            resultVO.setCode(0);
            resultVO.setMsg("删除成功，删除"+rows+"条数");
            resultVO.setData(rows);
        }else if(books == null){
            resultVO.setCode(-1);
            resultVO.setMsg("删除失败，没有找到对应数据");
            resultVO.setData(null);
        }else{
            resultVO.setCode(-2);
            resultVO.setMsg("删除失败，请先清空该书本对应馆藏具体书籍");
            resultVO.setData(null);
        }
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectBookList(List<Integer> ids) {  // 批量删除书籍
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Books::getBookId,ids);
        List<Books> bookList = booksMapper.selectList(queryWrapper);
        ResultVO resultVO = new ResultVO();
        List<Books> validateList = new ArrayList<>();
        if(bookList != null && bookList.size() > 0){
            for(Books book : bookList){
                if(book.getTotalCopies() == 0){
                    validateList.add(book);
                }else{
                    resultVO.setCode(-1);
                    resultVO.setMsg("请检查选中书籍的总馆藏数量是否为0！！！");
                    resultVO.setData(null);
                    return resultVO;
                }
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setMsg("请检查书籍是否存在");
            resultVO.setData(null);
            return resultVO;
        }
        int rows = booksMapper.delete(queryWrapper);
        if(rows > 0 && validateList.size() > 0){
            resultVO.setCode(0);
            resultVO.setMsg("成功删除"+rows+"条书籍信息");
            resultVO.setData(rows);
        }
        return resultVO;
    }

    @Override
    public ResultVO editSelectBook(EditBookForm editBookForm) {  // 修改选中书籍
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Books::getBookId,editBookForm.getBookId());
        ResultVO resultVO = new ResultVO();
        Books books = booksMapper.selectOne(queryWrapper);
        if(books != null){
            books.setIsbn(editBookForm.getIsbn());
            books.setTitle(editBookForm.getTitle());
            books.setAuthor(editBookForm.getAuthor());
            books.setPublisher(editBookForm.getPublisher());
            books.setPublishDate(editBookForm.getPublishDate());
            books.setPrice(editBookForm.getPrice());
            books.setSummary(editBookForm.getSummary());
            int rows = booksMapper.updateById(books);
            if(rows > 0){
                resultVO.setCode(0);
                resultVO.setMsg("修改成功，已更新该书籍信息");
                resultVO.setData(rows);
            }else{
                resultVO.setCode(-2);
                resultVO.setMsg("修改失败");
                resultVO.setData(null);
            }
        }else{
            resultVO.setCode(-1);
            resultVO.setMsg("没有找到对应书籍信息！");
            resultVO.setData(null);
            return resultVO;
        }
        return resultVO;
    }

    @Override
    public ResultVO addBook(AddBookForm addBookForm) {  // 新增书籍
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate time = LocalDate.parse(addBookForm.getPublishDate(), formatter);
        ResultVO resultVO = new ResultVO();
        if(addBookForm != null){
            Books book = new Books();
            book.setIsbn(addBookForm.getIsbn());
            book.setTitle(addBookForm.getTitle());
            book.setAuthor(addBookForm.getAuthor());
            book.setPublisher(addBookForm.getPublisher());
            book.setPublishDate(time);
            book.setPrice(addBookForm.getPrice());
            book.setSummary(addBookForm.getSummary());
            book.setCategoryId(addBookForm.getCategoryId());
            book.setCoverImage(addBookForm.getCoverImage());
            int rows = booksMapper.insert(book);
            if(rows > 0){
                resultVO.setCode(0);
                resultVO.setMsg("新增书籍成功");
                resultVO.setData(rows);
            }else{
                resultVO.setCode(-2);
                resultVO.setMsg("新增书籍失败，请检查书名、分类ID是否存在");
                resultVO.setData(null);
            }
        }else {
            resultVO.setCode(-1);
            resultVO.setMsg("新增书籍失败，请检查请求上传的数据");
        }
        return resultVO;
    }

    @Override
    public ResultVO addBatchBook(MultipartFile file) {  // 批量录入新书籍
        ResultVO resultVO = new ResultVO();

        // 1.检查上传的excel文件是否为空
        if(file == null){
            resultVO.setCode(-1);
            resultVO.setMsg("请上传excel文件!!!");
            return resultVO;
        }

        // 2. 检查上传的文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null ||
                (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx"))) {
            resultVO.setCode(-2);
            resultVO.setMsg("请上传正确的excel文件（.xls或.xlsx格式）!!!");
            return resultVO;
        }

        // 3.开始读取数据
        List<AddBookExcelForm> excelForms = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), AddBookExcelForm.class,
                    new AnalysisEventListener<AddBookExcelForm>() {
                        @Override
                        public void invoke(AddBookExcelForm data, com.alibaba.excel.context.AnalysisContext context) {
                            excelForms.add(data);
                        }
                        @Override
                        public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {

                        }
                    }).sheet().doRead();
        } catch (IOException e) {
            resultVO.setCode(-1);
            resultVO.setMsg("读取Excel文件失败");
            return resultVO;
        }

        int successCount = 0;
        int failCount = 0;
        List<Books> booksList = new ArrayList<>();
        for (AddBookExcelForm excelForm : excelForms) {
            // 基础校验
            if (excelForm.getTitle() == null || excelForm.getTitle().trim().isEmpty() ||
                    excelForm.getCategoryId() == null ||
                    excelForm.getPrice() == null || excelForm.getPrice() < 0) {
                resultVO.setCode(-2);
                resultVO.setMsg("请严格检查上传的Excel文件是否规范");
                return resultVO;
            }

            // 保存
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate time = LocalDate.parse(excelForm.getPublishDate(), formatter);
                Books book = new Books();
                book.setIsbn(excelForm.getIsbn());
                book.setTitle(excelForm.getTitle());
                book.setAuthor(excelForm.getAuthor());
                book.setPublisher(excelForm.getPublisher());
                book.setPublishDate(time);
                book.setSummary(excelForm.getSummary());
                book.setCategoryId(excelForm.getCategoryId());
                book.setCoverImage(excelForm.getCoverImage());
                booksList.add(book);
            } catch (Exception ignored) {
                resultVO.setCode(-2);
                resultVO.setMsg("请严格检查上传的Excel文件是否规范");
                return resultVO;
            }
        }
        for(Books book : booksList){
            booksMapper.insert(book);
            successCount++;
        }
        resultVO.setCode(0);
        resultVO.setData(successCount);
        resultVO.setMsg("成功导入" + successCount + "本书籍");
        return resultVO;
    }

    @Override
    public ResultVO findBookByCategoryId(Integer currentId) {  // 通过分类ID查询书籍
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        List<Books> bookList = new ArrayList<>();
        // 1.根据现在的分类ID查询所有的Books对象
        if(currentId != null){
            queryWrapper.in(Books::getCategoryId,currentId);
            bookList = booksMapper.selectList(queryWrapper);
            if(bookList != null && bookList.size() > 0){
                resultVO.setCode(0);
                resultVO.setMsg("成功获取书籍分类对应卡片数据");
                resultVO.setData(bookList);
            }else{
                resultVO.setCode(-1);
                resultVO.setMsg("没有书籍分类对应卡片数据");
                resultVO.setData(null);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setMsg("获取书籍分类对应卡片数据失败");
        }

        return resultVO;
    }

    @Override
    public ResultVO getNewBookList() {  // 获取本月最新书籍
        // 获取本月创建的书籍返回
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Books> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m') = {0}",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        List<Books> list = booksMapper.selectList(queryWrapper);
        if(list == null || list.size()<1){
            resultVO.setCode(-1);
            resultVO.setMsg("本月暂无新录入书籍");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setData(list);
        resultVO.setMsg("获取最新书籍数据成功");
        return resultVO;
    }
}
