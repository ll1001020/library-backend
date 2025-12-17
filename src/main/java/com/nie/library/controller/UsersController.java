package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.form.LoginForm;
import com.nie.library.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm){
        ResultVO resultVO = this.iUsersService.login(loginForm);
        return resultVO;
    }
}

