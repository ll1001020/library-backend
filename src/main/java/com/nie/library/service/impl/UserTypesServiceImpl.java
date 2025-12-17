package com.nie.library.service.impl;

import com.nie.library.entity.UserTypes;
import com.nie.library.mapper.UserTypesMapper;
import com.nie.library.service.IUserTypesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户类型表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-12
 */
@Service
public class UserTypesServiceImpl extends ServiceImpl<UserTypesMapper, UserTypes> implements IUserTypesService {

}
