package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.MenuBack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
public interface IMenuBackService extends IService<MenuBack> {
    public ResultVO getMenuBackList();
}
