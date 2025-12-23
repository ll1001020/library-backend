package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.*;
import com.nie.library.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@RestController
@RequestMapping("/library/users")
public class UsersController {

    @Autowired
    IUsersService iUsersService;

    // 登录用户，同时区分登录是前台还是后台
    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm){
        ResultVO resultVO = this.iUsersService.login(loginForm);
        return resultVO;
    }

    // 获取所有用户信息
    @PostMapping("/getUser")
    public ResultVO getUser(@RequestBody PaginationForm paginationForm){
        ResultVO resultVO = this.iUsersService.getUser(paginationForm);
        return resultVO;
    }

    // 搜索用户
    @PostMapping("/searchUser")
    public ResultVO searchUser(@RequestBody SearchForm searchForm, PaginationForm paginationForm){
        ResultVO resultVO = this.iUsersService.searchUser(searchForm,paginationForm);
        return resultVO;
    }

    // 添加新用户
    @PostMapping("/addUser")
    public ResultVO addUser(@RequestBody AddUserForm addUserForm){
        ResultVO resultVO = this.iUsersService.addUser(addUserForm);
        return resultVO;
    }

    // 批量添加新用户
    @PostMapping("/addBatchUser")
    public ResultVO addBatchUser(@RequestParam("file") MultipartFile file){
        ResultVO resultVO = this.iUsersService.addBatchUser(file);
        return resultVO;
    }

    // 修改选中用户信息
    @PostMapping("/editSelectUser")
    public ResultVO editSelectUser(@RequestBody EditUserForm editUserForm){
        ResultVO resultVO = this.iUsersService.editSelectUser(editUserForm);
        return resultVO;
    }

    @GetMapping("/deleteSelectUser")
    public ResultVO deleteSelectUser(Integer id){
        ResultVO resultVO = this.iUsersService.deleteSelectUser(id);
        return resultVO;
    }

    @PostMapping("/deleteSelectUserList")
    public ResultVO deleteSelectUserList(@RequestBody List<Integer> ids){
        ResultVO resultVO = this.iUsersService.deleteSelectUserList(ids);
        return resultVO;
    }

    @GetMapping("/addUserInBlack")
    public ResultVO addUserInBlack(Integer id){
        ResultVO resultVO = this.iUsersService.addUserInBlack(id);
        return resultVO;
    }

    @GetMapping("/removeUserInBlack")
    public ResultVO removeUserInBlack(Integer id){
        ResultVO resultVO = this.iUsersService.removeUserInBlack(id);
        return resultVO;
    }

    @PostMapping("/getBlackUser")
    public ResultVO getBlackUser(@RequestBody PaginationForm paginationForm){
        ResultVO resultVO = this.iUsersService.getBlackUser(paginationForm);
        return resultVO;
    }

    @PostMapping("/searchBlackUser")
    public ResultVO searchBlackUser(@RequestBody SearchForm searchForm, PaginationForm paginationForm){
        ResultVO resultVO = this.iUsersService.searchBlackUser(searchForm,paginationForm);
        return resultVO;
    }

}

