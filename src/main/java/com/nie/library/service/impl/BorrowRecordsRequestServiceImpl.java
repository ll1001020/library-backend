package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.*;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.mapper.BorrowRecordsMapper;
import com.nie.library.mapper.BorrowRecordsRequestMapper;
import com.nie.library.mapper.UsersMapper;
import com.nie.library.service.BorrowRecordsRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
 * @since 2025-12-26
 */
@Service
public class BorrowRecordsRequestServiceImpl extends ServiceImpl<BorrowRecordsRequestMapper, BorrowRecordsRequest> implements BorrowRecordsRequestService {

    @Autowired
    private BorrowRecordsRequestMapper borrowRecordsRequestMapper;
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private BookCopiesMapper bookCopiesMapper;

    @Override
    public ResultVO applyBorrow(Users user, Integer id) {  // 申请借阅书籍
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecordsRequest> borrowQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Users> usersQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<BookCopies> bookCopiesQueryWrapper = new LambdaQueryWrapper<>();
        // 1.判断用户是否可以借书
        usersQueryWrapper.eq(Users::getUserId,user.getUserId());
        usersQueryWrapper.eq(Users::getStatus,"正常");
        Users currentUser = usersMapper.selectOne(usersQueryWrapper);
        if(currentUser==null){
            resultVO.setMsg("您已经拉黑，请联系管理员");
            resultVO.setCode(-4);
            return resultVO;
        }
        if((currentUser.getBorrowLimit()-currentUser.getBorrowCount()) < 0){
            resultVO.setCode(-1);
            resultVO.setMsg("您当前借阅的太多，请先阅读已借书籍！");
            return resultVO;
        }
        // 2.判断当前书籍是否有空余可借
        bookCopiesQueryWrapper.eq(BookCopies::getBookId,id);
        List<BookCopies> bookCopiesList = bookCopiesMapper.selectList(bookCopiesQueryWrapper);
        BookCopies currentBookCopies = null;
        for(BookCopies bookCopies:bookCopiesList){
            if(!bookCopies.getStatus().equals("在架")){
                continue;
            }else{
                currentBookCopies = bookCopies;
                break;
            }
        }
        if(currentBookCopies == null){
            resultVO.setCode(-2);
            resultVO.setMsg("很抱歉，当前没有空闲该书籍可以借阅。");
            return resultVO;
        }
        // 3.登记借书记录，该书籍状态变为'借阅待审'
        LocalDateTime now = LocalDateTime.now();
        BorrowRecordsRequest borrowRecordsRequest = new BorrowRecordsRequest();
        borrowRecordsRequest.setUserId(user.getUserId());
        borrowRecordsRequest.setUsername(user.getUsername());
        borrowRecordsRequest.setCopyId(currentBookCopies.getCopyId());
        borrowRecordsRequest.setTitle(currentBookCopies.getTitle());
        borrowRecordsRequest.setBorrowrequestDate(now);
        borrowRecordsRequest.setBorrowrequestStatus("申请中");
        int rows = borrowRecordsRequestMapper.insert(borrowRecordsRequest);
        currentBookCopies.setStatus("借阅待审");
        int rows1 = bookCopiesMapper.updateById(currentBookCopies);
        if(rows != 0 ){
            resultVO.setCode(0);
            resultVO.setMsg("申请借阅成功");
        }
        if(rows1 == 0){
            resultVO.setCode(-3);
            resultVO.setMsg("修改图书副本状态异常");
        }
        return resultVO;
    }

    @Override
    public ResultVO getBorrowRequest(PaginationForm paginationForm) {  // 获取所有申请借阅记录
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        Page<BorrowRecordsRequest> page = new Page<>(paginationForm.getCurrentPage(),paginationForm.getPageSize());
        Page<BorrowRecordsRequest> borrowPage = borrowRecordsRequestMapper.selectPage(page,queryWrapper);
        List<BorrowRecordsRequest> list = borrowPage.getRecords();
        if(list == null || list.size() == 0){
            resultVO.setCode(-1);
            resultVO.setMsg("当前没有申请记录");
            return resultVO;
        }
        PaginationVO paginationVO =new PaginationVO();
        paginationVO.setCurrentPage(borrowPage.getCurrent());
        paginationVO.setPageSize(borrowPage.getSize());
        paginationVO.setTotal(borrowPage.getTotal());
        resultVO.setMsg("成功获取申请记录");
        resultVO.setData(list);
        resultVO.setPaginationVO(paginationVO);
        resultVO.setCode(0);
        return resultVO;
    }

    @Override
    public ResultVO searchRequest(SearchForm searchForm, PaginationForm paginationForm) { // 搜素申请借阅记录
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.eq(BorrowRecordsRequest::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍名")){
                    queryWrapper.eq(BorrowRecordsRequest::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.eq(BorrowRecordsRequest::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.eq(BorrowRecordsRequest::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("申请状态")){
                    queryWrapper.eq(BorrowRecordsRequest::getBorrowrequestStatus, searchForm.getSearchContent().trim());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.like(BorrowRecordsRequest::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍名")){
                    queryWrapper.like(BorrowRecordsRequest::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.like(BorrowRecordsRequest::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("书籍ID")){
                    queryWrapper.like(BorrowRecordsRequest::getCopyId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("申请状态")){
                    queryWrapper.like(BorrowRecordsRequest::getBorrowrequestStatus, searchForm.getSearchContent().trim());
                }
            }
            if(searchForm.getPublisherStartTime() != null && !searchForm.getPublisherStartTime().isEmpty()){
                LocalDateTime startDate = LocalDateTime.parse(searchForm.getPublisherStartTime(), formatter);
//                System.out.println(searchForm.getPublisherStartTime());
                queryWrapper.ge(BorrowRecordsRequest::getBorrowrequestDate,startDate);
            }
            if(searchForm.getPublisherEndTime() != null && !searchForm.getPublisherEndTime().isEmpty()){
                LocalDateTime endDate = LocalDateTime.parse(searchForm.getPublisherEndTime(), formatter);
//                System.out.println(searchForm.getPublisherEndTime());
                queryWrapper.le(BorrowRecordsRequest::getBorrowrequestDate,endDate);
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<BorrowRecordsRequest> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<BorrowRecordsRequest> PageList = borrowRecordsRequestMapper.selectPage(page, queryWrapper);
        List<BorrowRecordsRequest> list = PageList.getRecords();
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
    public ResultVO permitRequest(Users user,Integer id) {  // 批准选中的借阅记录
        ResultVO resultVO = new ResultVO();
        // 要先判断该条申请是否首次批准
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowRecordsRequest::getRecordId, id);
        BorrowRecordsRequest request = borrowRecordsRequestMapper.selectOne(queryWrapper);
        if(!request.getBorrowrequestStatus().equals("申请中")){
            resultVO.setCode(-3);
            resultVO.setMsg("该记录已批阅过！");
            return resultVO;
        }
        // 1.更新借阅请求表

        request.setBorrowrequestStatus("已同意");
        request.setBorrowpermitDate(LocalDateTime.now());
        request.setCreatorId(user.getUserId());
        int rows1 = borrowRecordsRequestMapper.updateById(request);
        if(rows1 < 0 ){
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("批准失败，当前申请不存在，请刷新页面！");
            return resultVO;
        }

        // 2.检查用户是否存在
        LambdaQueryWrapper<Users> usersQueryWrapper = new LambdaQueryWrapper<>();
        usersQueryWrapper.eq(Users::getUserId,request.getUserId());
        Users user1 = usersMapper.selectOne(usersQueryWrapper);
        if(user1 == null){
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("批准失败，申请用户不存在，请删除该条申请！");
            return resultVO;
        }

        // 3.更新书籍副本表
        LambdaQueryWrapper<BookCopies> copyQueryWrapper = new LambdaQueryWrapper<>();
        copyQueryWrapper.eq(BookCopies::getCopyId,request.getCopyId());
        BookCopies copy = bookCopiesMapper.selectOne(copyQueryWrapper);
        if(copy == null){
            resultVO.setCode(-4);
            resultVO.setMsg("没找到借阅的书籍信息！！");
            return resultVO;
        }else{
            copy.setStatus("已借出");
            bookCopiesMapper.updateById(copy);
        }


        BorrowRecords record = new BorrowRecords();
        record.setUserId(request.getUserId());
        record.setUsername(request.getUsername());
        record.setCopyId(request.getCopyId());
        record.setTitle(request.getTitle());
        record.setBorrowDate(LocalDateTime.now());
        if(user1.getBorrowLimit() != 0){
            record.setDueDate(LocalDateTime.now().plusDays(user1.getBorrowLimitDay()));
        }else{
            record.setDueDate(LocalDateTime.now().plusYears(999));
        }
        record.setRenewCount(0);
        record.setCreatorId(user.getUserId());
        int rows2 = borrowRecordsMapper.insert(record);
        if(rows2 >0 ){
            resultVO.setCode(0);
            resultVO.setMsg("批准借阅成功，请前往借阅记录查看该条记录记载");
            return resultVO;
        }
        resultVO.setCode(-5);
        resultVO.setData(null);
        resultVO.setMsg("批准失败，插入新纪录失败！");
        return resultVO;
    }

    @Override
    public ResultVO permitRequestReject(Users user, Integer id) {  // 打回选中的借阅记录
        ResultVO resultVO = new ResultVO();
        // 1.首先判断该记录是否批阅过
        // 2.判断当前申请用户是否存在
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowRecordsRequest::getRecordId, id);
        BorrowRecordsRequest request = borrowRecordsRequestMapper.selectOne(queryWrapper);
        if(!request.getBorrowrequestStatus().equals("申请中")){
            resultVO.setCode(-1);
            resultVO.setMsg("该记录已批阅过！");
            return resultVO;
        }
        LambdaQueryWrapper<Users> usersQueryWrapper = new LambdaQueryWrapper<>();
        usersQueryWrapper.eq(Users::getUserId,request.getUserId());
        Users user1 = usersMapper.selectOne(usersQueryWrapper);
        if(user1 == null){
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("批准失败，申请用户不存在，请删除该条申请！");
            return resultVO;
        }
        request.setBorrowpermitDate(LocalDateTime.now());
        request.setCreatorId(user.getUserId());
        request.setBorrowrequestStatus("已打回");
        int rows1 = borrowRecordsRequestMapper.updateById(request);
        if(rows1 != 1 ){
            resultVO.setCode(-2);
            resultVO.setMsg("打回申请出错，请重新操作");
            return resultVO;
        }
        LambdaQueryWrapper<BookCopies> copyQueryWrapper = new LambdaQueryWrapper<>();
        copyQueryWrapper.eq(BookCopies::getCopyId,request.getCopyId());
        BookCopies copy = bookCopiesMapper.selectOne(copyQueryWrapper);
        copy.setStatus("在架");
        int row2 = bookCopiesMapper.updateById(copy);
        if(row2!=1){
            resultVO.setCode(-3);
            resultVO.setMsg("更新副本数据失败！");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setData(null);
        resultVO.setMsg("成功打回该申请！");
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectRequest(List<Integer> ids) {  // 批量删除选中的借阅记录
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BorrowRecordsRequest::getRecordId,ids);
        List<BorrowRecordsRequest> list = borrowRecordsRequestMapper.selectList(queryWrapper);
        List<BorrowRecordsRequest> validateList = new ArrayList<>();
        int rows = 0;
        if(list != null && list.size() > 0){
            for(BorrowRecordsRequest request : list){
                if(!request.getBorrowrequestDate().plusDays(60).isBefore(LocalDateTime.now())){
                    resultVO.setCode(-1);
                    resultVO.setData(null);
                    resultVO.setMsg("仅允许删除申请超过60天的记录！");
                    return resultVO;
                }
                validateList.add(request);
            }
        }
        if(validateList.size() > 0){
            for(BorrowRecordsRequest request : validateList){
                borrowRecordsRequestMapper.deleteById(request.getRecordId());
                rows++;
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("没找到选中的申请！");
        }
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setData(rows);
            resultVO.setMsg("成功删除"+rows+"条申请");
        }
        return resultVO;
    }
}
