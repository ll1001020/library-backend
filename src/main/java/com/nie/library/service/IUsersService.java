package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.LoginForm;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IUsersService extends IService<Users> {
    public ResultVO login(LoginForm loginForm);
}
