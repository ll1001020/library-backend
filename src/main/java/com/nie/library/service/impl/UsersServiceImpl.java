package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.UserTypes;
import com.nie.library.entity.Users;
import com.nie.library.form.LoginForm;
import com.nie.library.mapper.UsersMapper;
import com.nie.library.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResultVO login(LoginForm loginForm) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        // 1.判断用户选择登录的类型
        usersQueryWrapper.eq("user_type_id", loginForm.getLoginType());
        // 2.判断用户是否存在
        usersQueryWrapper.eq("username", loginForm.getUsername());
        Users user = usersMapper.selectOne(usersQueryWrapper);

        if(user == null){
            resultVO.setCode(-1);
            resultVO.setMsg("用户不存在");
        }else{
            // 3.判断密码是否正确
            if (!user.getPassword().equals(loginForm.getPassword())){
                resultVO.setCode(-2);
                resultVO.setMsg("密码错误");
            }else{
                resultVO.setCode(0);
                resultVO.setData(user);
                resultVO.setMsg("登录成功");
            }
        }
        return resultVO;
    }
}
