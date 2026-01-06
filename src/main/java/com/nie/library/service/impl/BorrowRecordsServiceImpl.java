package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCopies;
import com.nie.library.entity.BorrowRecords;
import com.nie.library.entity.BorrowRecordsRequest;
import com.nie.library.entity.Users;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.mapper.BorrowRecordsMapper;
import com.nie.library.mapper.UsersMapper;
import com.nie.library.service.IBorrowRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借阅记录表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class BorrowRecordsServiceImpl extends ServiceImpl<BorrowRecordsMapper, BorrowRecords> implements IBorrowRecordsService {

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    @Autowired
    private BookCopiesMapper bookCopiesMapper;
    // 注入用户Mapper
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public ResultVO getBorrowRecord(PaginationForm paginationForm) {  // 获取所有借阅记录
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecords> queryWrapper = new LambdaQueryWrapper<>();
        Page<BorrowRecords> page = new Page<BorrowRecords>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<BorrowRecords> recordPage = borrowRecordsMapper.selectPage(page, queryWrapper);
        List<BorrowRecords> records = recordPage.getRecords();
        if(records == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前无借阅记录");
            return resultVO;
        }
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(recordPage.getCurrent());
        paginationVO.setPageSize(recordPage.getSize());
        paginationVO.setTotal(recordPage.getTotal());
        resultVO.setCode(0);
        resultVO.setMsg("成功获取记录"+records.size()+"条");
        resultVO.setData(records);
        resultVO.setPaginationVO(paginationVO);
        return resultVO;
    }

    @Override
    public ResultVO searchRecord(SearchForm searchForm, PaginationForm paginationForm) {  // 搜索借阅记录
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecords> queryWrapper = new LambdaQueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.eq(BorrowRecords::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍名")){
                    queryWrapper.eq(BorrowRecords::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.eq(BorrowRecords::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.eq(BorrowRecords::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("借阅状态")){
                    queryWrapper.eq(BorrowRecords::getBorrowStatus, searchForm.getSearchContent().trim());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.like(BorrowRecords::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍名")){
                    queryWrapper.like(BorrowRecords::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.like(BorrowRecords::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.like(BorrowRecords::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("借阅状态")){
                    queryWrapper.like(BorrowRecords::getBorrowStatus, searchForm.getSearchContent().trim());
                }
            }
            if(searchForm.getPublisherStartTime() != null && !searchForm.getPublisherStartTime().isEmpty()){
                LocalDateTime startDate = LocalDateTime.parse(searchForm.getPublisherStartTime(), formatter);
//                System.out.println(searchForm.getPublisherStartTime());
                queryWrapper.ge(BorrowRecords::getDueDate,startDate);
            }
            if(searchForm.getPublisherEndTime() != null && !searchForm.getPublisherEndTime().isEmpty()){
                LocalDateTime endDate = LocalDateTime.parse(searchForm.getPublisherEndTime(), formatter);
//                System.out.println(searchForm.getPublisherEndTime());
                queryWrapper.le(BorrowRecords::getDueDate,endDate);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<BorrowRecords> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<BorrowRecords> PageList = borrowRecordsMapper.selectPage(page, queryWrapper);
        List<BorrowRecords> list = PageList.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(PageList.getCurrent());
        paginationVO.setPageSize(PageList.getSize());
        paginationVO.setTotal(PageList.getTotal());
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
    public ResultVO borrowReturn(Users user, Integer id) {  // 登记选中借阅记录归还
        ResultVO resultVO = new ResultVO();
        // 1.找出该条借阅记录
        LambdaQueryWrapper<BorrowRecords> borrowQueryWrapper = new LambdaQueryWrapper<>();
        borrowQueryWrapper.eq(BorrowRecords::getRecordId, id);
        BorrowRecords currentRecord = borrowRecordsMapper.selectOne(borrowQueryWrapper);
        // 2.修改记录的借阅状态和操作人员ID，登记归还日期
        currentRecord.setActualReturnDate(LocalDateTime.now());
        currentRecord.setBorrowStatus("已归还");
        currentRecord.setCreatorId(user.getUserId());
        int row1 = borrowRecordsMapper.updateById(currentRecord);
        if(row1 == 0){
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("归还失败");
            return resultVO;
        }
        // 3.根据记录记载的书籍副本ID去找到具体馆藏书籍，更新馆藏状态。
        LambdaQueryWrapper<BookCopies> bookCopiesQueryWrapper = new LambdaQueryWrapper<>();
        bookCopiesQueryWrapper.eq(BookCopies::getCopyId, currentRecord.getCopyId());
        BookCopies currentCopy = bookCopiesMapper.selectOne(bookCopiesQueryWrapper);
        currentCopy.setStatus("在架");
        int row2 = bookCopiesMapper.updateById(currentCopy);
        if(row2 == 0){
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("更新馆藏书籍副本信息失败");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setMsg("归还成功");
        return resultVO;
    }

    @Override
    public ResultVO borrowRenew(Users user, Integer id) {  // 为选中记录续借一次
        ResultVO resultVO = new ResultVO();
        // 还是先判断用户是否被拉黑
        if(user.getStatus().equals("禁用")){
            resultVO.setCode(-3);
            resultVO.setData(null);
            resultVO.setMsg("用户已被拉黑，无法续借！");
            return resultVO;
        }
        // 1.获取当前选中记录
        LambdaQueryWrapper<BorrowRecords> borrowQueryWrapper = new LambdaQueryWrapper<>();
        borrowQueryWrapper.eq(BorrowRecords::getRecordId, id);
        BorrowRecords currentRecord = borrowRecordsMapper.selectOne(borrowQueryWrapper);
        // 2.更新选中记录的截止时间，续借默认一次添加15天，最多续借三次
        if(currentRecord.getRenewCount() == 3){
            resultVO.setCode(-1);
            resultVO.setMsg("当前用户已续借多次，不可续借！");
            return resultVO;
        }
        currentRecord.setDueDate(currentRecord.getDueDate().plusDays(15));
        currentRecord.setRenewCount(currentRecord.getRenewCount() + 1);
        // 3.更新操作人ID
        currentRecord.setCreatorId(user.getUserId());
        int row1 = borrowRecordsMapper.updateById(currentRecord);
        if(row1 == 0){
            resultVO.setCode(-2);
            resultVO.setMsg("续借失败");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setMsg("续借登记成功！");
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectBorrowList(List<Integer> ids) {  // 批量删除选中的借阅记录
        ResultVO resultVO = new ResultVO();
        // 1.获取当前选中的所有借阅记录
        LambdaQueryWrapper<BorrowRecords> borrowQueryWrapper = new LambdaQueryWrapper<>();
        borrowQueryWrapper.in(BorrowRecords::getRecordId, ids);
        List<BorrowRecords> borrowRecordList = borrowRecordsMapper.selectList(borrowQueryWrapper);
        // 2.检查当前选中的所有借阅记录是否符合要求（必须是已归还状态且超过归还时间60日）
        List<BorrowRecords> currentRecordList = new ArrayList<>();
        for(BorrowRecords borrowRecord : borrowRecordList){
            if(!borrowRecord.getBorrowStatus().equals("已归还")
                || !borrowRecord.getActualReturnDate().plusDays(60).isBefore(LocalDateTime.now())
            ){
                resultVO.setCode(-1);
                resultVO.setMsg("只允许删除归还超过60天的记录！");
                return resultVO;
            }
            currentRecordList.add(borrowRecord);
        }
        // 3.全部符合则删除选中的所有借阅记录
        int rows = 0;
        for(BorrowRecords borrowRecord : currentRecordList){
            int i = borrowRecordsMapper.deleteById(borrowRecord);
            if(i != 0){
                rows++;
            }
        }
        if(rows != currentRecordList.size()){
            resultVO.setCode(-2);
            resultVO.setMsg("删除未完全，请检查");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setMsg("成功删除"+rows+"条借阅记录");
        return resultVO;
    }
}
