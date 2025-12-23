package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IUsersService extends IService<Users> {
    // 登录用户，同时区分登录是前台还是后台
    public ResultVO login(LoginForm loginForm);

    // 获取所有用户信息
    public ResultVO getUser(PaginationForm paginationForm);

    // 搜索用户
    public ResultVO searchUser(SearchForm searchForm, PaginationForm paginationForm);

    // 添加新用户
    public ResultVO addUser(AddUserForm addUserForm);

    // 修改选中用户信息
    public ResultVO editSelectUser(EditUserForm editUserForm);

    // 批量添加新用户
    public ResultVO addBatchUser(MultipartFile file);

    // 删除选中用户
    public ResultVO deleteSelectUser(Integer id);

    // 批量删除选中用户
    public ResultVO deleteSelectUserList(List<Integer> ids);

    // 添加用户进黑名单
    public ResultVO addUserInBlack(Integer id);

    // 从黑名单中移除用户
    public ResultVO removeUserInBlack(Integer id);

    // 获取黑名单用户列表
    public ResultVO getBlackUser(PaginationForm paginationForm);

    // 查询黑名单用户
    public ResultVO searchBlackUser(SearchForm searchForm, PaginationForm paginationForm);

}
