package com.nie.library.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.DTO.PersonalDTO;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.*;
import com.nie.library.form.*;
import com.nie.library.mapper.BorrowRecordsMapper;
import com.nie.library.mapper.BorrowRecordsRequestMapper;
import com.nie.library.mapper.UsersMapper;
import com.nie.library.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private UsersMapper usersMapper;
    // 注入加密解密
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 注入记录Mapper
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    // 注入申请Mapper
    @Autowired
    private BorrowRecordsRequestMapper borrowRecordsRequestMapper;

    @Override
    public ResultVO login(LoginForm loginForm) {
        QueryWrapper<Users> usersQueryWrapper1 = new QueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        // 1.判断用户选择登录的类型
        usersQueryWrapper1.eq("user_type_id", loginForm.getLoginType());
        // 2.判断用户是否存在
        usersQueryWrapper1.eq("username", loginForm.getUsername());
        Users user = usersMapper.selectOne(usersQueryWrapper1);
        LambdaQueryWrapper<Users> usersQueryWrapper2 = new LambdaQueryWrapper<>();
        usersQueryWrapper2.eq(Users::getStudentId,loginForm.getUsername());
        Users user2 = usersMapper.selectOne(usersQueryWrapper2);

        if(user == null && user2 == null){
            resultVO.setCode(-1);
            resultVO.setMsg("用户不存在");
        }else if(user != null && user2 == null){
            // 3.判断密码是否正确
            boolean pass = passwordEncoder.matches(loginForm.getPassword().trim(),user.getPassword());
            if (!pass){
                resultVO.setCode(-2);
                resultVO.setMsg("密码错误");
            }else{
                LocalDateTime now = LocalDateTime.now();
                user.setLastLoginTime(now);
                usersMapper.updateById(user);
                resultVO.setCode(0);
                resultVO.setData(user);
                resultVO.setMsg("登录成功");
            }
        }else if(user == null && user2 != null){
            boolean pass = passwordEncoder.matches(loginForm.getPassword().trim(),user2.getPassword());
            if (!pass){
                resultVO.setCode(-2);
                resultVO.setMsg("密码错误");
            }else{
                LocalDateTime now = LocalDateTime.now();
                user.setLastLoginTime(now);
                usersMapper.updateById(user);
                resultVO.setCode(0);
                resultVO.setData(user2);
                resultVO.setMsg("登录成功");
            }
        }else{
            boolean pass1 = passwordEncoder.matches(loginForm.getPassword().trim(),user.getPassword());
            boolean pass2 = passwordEncoder.matches(loginForm.getPassword().trim(),user2.getPassword());
            if (!pass1){
                if (!pass2){
                    resultVO.setCode(-2);
                    resultVO.setMsg("密码错误");
                }else{
                    LocalDateTime now = LocalDateTime.now();
                    user.setLastLoginTime(now);
                    usersMapper.updateById(user);
                    resultVO.setCode(0);
                    resultVO.setData(user2);
                    resultVO.setMsg("登录成功");
                }
            }else{
                LocalDateTime now = LocalDateTime.now();
                user.setLastLoginTime(now);
                usersMapper.updateById(user);
                resultVO.setCode(0);
                resultVO.setData(user);
                resultVO.setMsg("登录成功");
            }
        }
        return resultVO;
    }

    @Override
    public ResultVO getUser(PaginationForm paginationForm) {
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> usersQueryWrapper = new LambdaQueryWrapper<>();
        Page<Users> page = new Page<>(paginationForm.getCurrentPage(),paginationForm.getPageSize());
        Page<Users> usersPage = usersMapper.selectPage(page, usersQueryWrapper);
        List<Users> usersList = usersPage.getRecords();
        if(usersList != null && usersList.size() > 0){
//            for(Users user : usersList){
//                user.setPassword(null);
//            }
            PaginationVO paginationVO = new PaginationVO();
            paginationVO.setCurrentPage(usersPage.getCurrent());
            paginationVO.setPageSize(usersPage.getSize());
            paginationVO.setTotal(usersPage.getTotal());
            resultVO.setCode(0);
            resultVO.setData(usersList);
            resultVO.setPaginationVO(paginationVO);
            resultVO.setMsg("成功获取到用户列表数据");
        }else {
            resultVO.setCode(-1);
            resultVO.setMsg("当前没有任何用户数据");
        }
        return resultVO;
    }

    @Override
    public ResultVO searchUser(SearchForm searchForm, PaginationForm paginationForm) { // 搜索用户
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("学号")){
                    queryWrapper.eq(Users::getStudentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.eq(Users::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.eq(Users::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("电话号码")){
                    queryWrapper.eq(Users::getPhone, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("真名")){
                    queryWrapper.eq(Users::getRealName, searchForm.getSearchContent());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("学号")){
                    queryWrapper.like(Users::getStudentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.like(Users::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.like(Users::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("电话号码")){
                    queryWrapper.like(Users::getPhone, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("真名")){
                    queryWrapper.like(Users::getRealName, searchForm.getSearchContent());
                }
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<Users> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<Users> userPageList = usersMapper.selectPage(page, queryWrapper);
        List<Users> list = userPageList.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(userPageList.getCurrent());
        paginationVO.setPageSize(userPageList.getSize());
        paginationVO.setTotal(userPageList.getTotal());
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
    public ResultVO addUser(AddUserForm addUserForm) {  // 添加新用户
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        // 1.先校验上传的新用户信息
        if(addUserForm.getUsername() == null || addUserForm.getUsername().trim().equals("")
                || addUserForm.getPassword() == null || addUserForm.getPassword().trim().equals("")
                || addUserForm.getStudentId() == null || addUserForm.getStudentId().trim().equals("")
                || addUserForm.getStatus() == null
        ){
            resultVO.setCode(-1);
            resultVO.setMsg("不符合规范，请严格检查上传信息！");
            return resultVO;
        }
        List<Users> userList = usersMapper.selectList(queryWrapper);
        for(Users user : userList){
            if(user.getStudentId().equals(addUserForm.getStudentId())
                || user.getUsername().equals(addUserForm.getUsername())
                    || user.getPhone().equals(addUserForm.getPhone())
            ){
                resultVO.setCode(-2);
                resultVO.setMsg("用户名、手机号和学号已存在，请重新添加！");
                return resultVO;
            }
        }
        // 2.信息无误后，记下时间开始添加新用户
        Users user = new Users();
        if(addUserForm.getUserTypeId() == 0){
            user.setBorrowLimit(0);
            user.setBorrowCount(-1);
            user.setBorrowLimitDay(0);
        }
        LocalDateTime now = LocalDateTime.now();
        String encodedPassword = passwordEncoder.encode(addUserForm.getPassword().trim());
        user.setUsername(addUserForm.getUsername());
        user.setPassword(encodedPassword);
        user.setRealName(addUserForm.getRealName());
        user.setStudentId(addUserForm.getStudentId());
        user.setPhone(addUserForm.getPhone());
        user.setUserTypeId(addUserForm.getUserTypeId());
        user.setStatus(addUserForm.getStatus());
        user.setRegisterTime(now);
        user.setNotes(addUserForm.getNotes());
        int rows =  usersMapper.insert(user);
        // 3.添加成功返回
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setMsg("成功添加"+rows+"条记录");
            resultVO.setData(rows);
        }
        return resultVO;
    }

    @Override
    public ResultVO editSelectUser(EditUserForm editUserForm) {  // 修改选中用户信息
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        // 1.判断用户是否存在
        queryWrapper.eq(Users::getUserId, editUserForm.getUserId());
        Users user = usersMapper.selectOne(queryWrapper);
        if(user == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前选中用户不存在，请刷新页面重新尝试！");
            return resultVO;
        }
        // 2.检验字段用户名、学号是否为空
        if(editUserForm.getStudentId() == null || editUserForm.getStudentId().isEmpty()
        || editUserForm.getUsername() == null || editUserForm.getUsername().isEmpty()){
            resultVO.setCode(-2);
            resultVO.setMsg("用户名、学号不可为空！！！");
            return resultVO;
        }
        // 3.检验密码是否为空，若为空则不修改密码
        String encodedPassword = passwordEncoder.encode(editUserForm.getPassword().trim());
        if(editUserForm.getPassword() != null || !editUserForm.getPassword().trim().isEmpty()){
            user.setPassword(encodedPassword);
        }
        // 4.没有问题后将修改后的数据覆写
        user.setUsername(editUserForm.getUsername());
        user.setRealName(editUserForm.getRealName());
        user.setStudentId(editUserForm.getStudentId());
        user.setPhone(editUserForm.getPhone());
        user.setStatus(editUserForm.getStatus());
        usersMapper.updateById(user);
        resultVO.setCode(0);
        resultVO.setData(user);
        resultVO.setMsg("修改成功！");
        return resultVO;
    }

    @Override
    public ResultVO addBatchUser(MultipartFile file) {  // 批量录入用户
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
        List<AddUserExcelForm> excelForms = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), AddUserExcelForm.class,
                    new AnalysisEventListener<AddUserExcelForm>() {
                        @Override
                        public void invoke(AddUserExcelForm data, com.alibaba.excel.context.AnalysisContext context) {
                            excelForms.add(data);
                        }
                        @Override
                        public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {}
                    }).sheet().doRead();
        } catch (IOException e) {
            resultVO.setCode(-1);
            resultVO.setMsg("读取Excel文件失败");
            return resultVO;
        }

        int successCount = 0;
        int failCount = 0;
        List<Users> userList = new ArrayList<>();
        for (AddUserExcelForm excelForm : excelForms) {
            // 基础校验
            LambdaQueryWrapper<Users> booksQueryWrapper = new LambdaQueryWrapper<>();
            List<Users> allUserList = usersMapper.selectList(booksQueryWrapper);
            // 先校验手机号、用户名、学号有没有重复的
            for(Users user : allUserList){
                if(user.getUsername().equals(excelForm.getUsername())){
                    resultVO.setCode(-3);
                    resultVO.setMsg("用户名有重复，请仔细检查Excel文件！");
                    return resultVO;
                }
                if(user.getPhone().equals(excelForm.getPhone())){
                    resultVO.setCode(-4);
                    resultVO.setMsg("手机号有重复，请仔细检查Excel文件！");
                    return resultVO;
                }
                if(user.getStudentId().equals(excelForm.getStudentId())){
                    resultVO.setCode(-5);
                    resultVO.setMsg("学号有重复，请仔细检查Excel文件！");
                    return resultVO;
                }
            }
            // 检查是否非空
            if (excelForm.getUsername() == null || excelForm.getUsername().trim().isEmpty()
                    || excelForm.getPassword() == null || excelForm.getPassword().trim().isEmpty()
                    || excelForm.getStudentId() == null || excelForm.getStudentId().trim().isEmpty()
                    || excelForm.getUserTypeId() == null
            ) {
                resultVO.setCode(-2);
                resultVO.setMsg("请严格检查上传的Excel文件是否规范完整");
                return resultVO;
            }

            // 保存
            try {
                Users user = new Users();
                if(excelForm.getUserTypeId() == 0){
                    user.setBorrowLimit(0);
                    user.setBorrowCount(-1);
                    user.setBorrowLimitDay(0);
                }
                LocalDateTime now = LocalDateTime.now();
                String encodedPassword = passwordEncoder.encode(excelForm.getPassword());
                user.setUsername(excelForm.getUsername());
                user.setPassword(encodedPassword);
                user.setRealName(excelForm.getRealName());
                user.setStudentId(excelForm.getStudentId());
                user.setPhone(excelForm.getPhone());
                user.setUserTypeId(excelForm.getUserTypeId());
                user.setStatus(excelForm.getStatus());
                user.setRegisterTime(now);
                user.setNotes(excelForm.getNotes());
                userList.add(user);
            } catch (Exception ignored) {
                resultVO.setCode(-2);
                resultVO.setMsg("请严格检查上传的Excel文件是否规范完整");
                return resultVO;
            }
        }
        for(Users user : userList){
            usersMapper.insert(user);
            successCount++;
        }
        resultVO.setCode(0);
        resultVO.setData(successCount);
        resultVO.setMsg("成功新增" + successCount + "位用户");
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectUser(Integer id) {  // 删除选中用户
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(Users::getUserId,id);
        Users user = usersMapper.selectOne(userQueryWrapper);
        if(user == null ){
            resultVO.setCode(-1);
            resultVO.setMsg("当前选中用户不存在，请刷新页面重新选择");
            return resultVO;
        }
        int rows = usersMapper.delete(userQueryWrapper);
        resultVO.setCode(0);
        resultVO.setData(rows);
        resultVO.setMsg("成功删除"+rows+"条记录");
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectUserList(List<Integer> ids) {  // 批量删除选中用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Users::getUserId,ids);
        List<Users> userList = usersMapper.selectList(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if(userList != null && userList.size() > 0){
            int rows = usersMapper.delete(queryWrapper);
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
            resultVO.setMsg("选中用户不存在，请重新选择");
        }
        return resultVO;
    }

    @Override
    public ResultVO addUserInBlack(Integer id) {
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserId,id);
        Users user = usersMapper.selectOne(queryWrapper);
        if(user == null){
            resultVO.setCode(-1);
            resultVO.setMsg("没有找到该用户，请核对用户ID");
            return resultVO;
        }
        user.setStatus("禁用");
        int rows =  usersMapper.updateById(user);
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setMsg("拉黑成功");
        }
        return resultVO;
    }

    @Override
    public ResultVO removeUserInBlack(Integer id) {
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserId,id);
        Users user = usersMapper.selectOne(queryWrapper);
        if(user == null){
            resultVO.setCode(-1);
            resultVO.setMsg("没有找到该用户，请核对用户ID");
            return resultVO;
        }
        user.setStatus("正常");
        int rows =  usersMapper.updateById(user);
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setMsg("移除成功");
        }
        return resultVO;
    }

    @Override
    public ResultVO getBlackUser(PaginationForm paginationForm) {
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> usersQueryWrapper = new LambdaQueryWrapper<>();
        usersQueryWrapper.eq(Users::getStatus,"禁用");
        Page<Users> page = new Page<>(paginationForm.getCurrentPage(),paginationForm.getPageSize());
        Page<Users> usersPage = usersMapper.selectPage(page, usersQueryWrapper);
        List<Users> usersList = usersPage.getRecords();
        if(usersList != null && usersList.size() > 0){
            for(Users user : usersList){
                user.setPassword(null);
            }
            PaginationVO paginationVO = new PaginationVO();
            paginationVO.setCurrentPage(usersPage.getCurrent());
            paginationVO.setPageSize(usersPage.getSize());
            paginationVO.setTotal(usersPage.getTotal());
            resultVO.setCode(0);
            resultVO.setData(usersList);
            resultVO.setPaginationVO(paginationVO);
            resultVO.setMsg("成功获取到黑名单用户列表数据");
        }else {
            resultVO.setCode(-1);
            resultVO.setMsg("当前没有任何用户数据");
        }
        return resultVO;
    }

    @Override
    public ResultVO searchBlackUser(SearchForm searchForm, PaginationForm paginationForm) {
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getStatus,"禁用");
        // 1.判断前端查询的是哪一个字段
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("学号")){
                    queryWrapper.eq(Users::getStudentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.eq(Users::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.eq(Users::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("电话号码")){
                    queryWrapper.eq(Users::getPhone, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("真名")){
                    queryWrapper.eq(Users::getRealName, searchForm.getSearchContent());
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("学号")){
                    queryWrapper.like(Users::getStudentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户名")){
                    queryWrapper.like(Users::getUsername, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("用户ID")){
                    queryWrapper.like(Users::getUserId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("电话号码")){
                    queryWrapper.like(Users::getPhone, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("真名")){
                    queryWrapper.like(Users::getRealName, searchForm.getSearchContent());
                }
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        Page<Users> page = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<Users> userPageList = usersMapper.selectPage(page, queryWrapper);
        List<Users> list = userPageList.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(userPageList.getCurrent());
        paginationVO.setPageSize(userPageList.getSize());
        paginationVO.setTotal(userPageList.getTotal());
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
    public ResultVO getPersonalBorrowInfoByUserId(Integer id) {  // 获取当前用户所有借阅相关信息，根据用户ID
        ResultVO resultVO = new ResultVO();
        // 先获取当前用户的借阅记录
        LambdaQueryWrapper<BorrowRecords> recordQueryWrapper = new LambdaQueryWrapper<>();
        recordQueryWrapper.eq(BorrowRecords::getUserId, id);
        List<BorrowRecords> currentList = new ArrayList<>(); // 定义一个集合用来存储当前借阅记录
        List<BorrowRecords> pastList = new ArrayList<>(); // 定义一个集合用来存储历史借阅记录
        // 判断当前借阅记录是否已经完成，若是已经完成，则划分为历史记录，若是未完成，则为当前记录
        List<BorrowRecords> allList = borrowRecordsMapper.selectList(recordQueryWrapper);
        for(BorrowRecords borrowRecords : allList){
            if(borrowRecords.getBorrowStatus().equals("借阅中")){
                currentList.add(borrowRecords);
            }else{
                LocalDateTime actualReturnDate = borrowRecords.getActualReturnDate();
                if (actualReturnDate != null && actualReturnDate.isAfter(LocalDateTime.now().minusDays(3))) {
                    pastList.add(borrowRecords);
                }
            }
        }
        // 在获取用户的预约记录
        LambdaQueryWrapper<BorrowRecordsRequest> requestQueryWrapper = new LambdaQueryWrapper<>();
        requestQueryWrapper.eq(BorrowRecordsRequest::getUserId, id);
        List<BorrowRecordsRequest> requestList = borrowRecordsRequestMapper.selectList(requestQueryWrapper);
        // 将获取到的记录全部封装到PersonalDTO中返回给前端
        PersonalDTO personalDTO = new PersonalDTO();
        personalDTO.setCurrentBorrow(currentList);
        personalDTO.setPastBorrow(pastList);
        personalDTO.setRequestBorrow(requestList);
        if(personalDTO == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前用户暂无任何借阅记录");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setMsg("成功获取借阅记录");
        resultVO.setData(personalDTO);
        return resultVO;
    }


}
