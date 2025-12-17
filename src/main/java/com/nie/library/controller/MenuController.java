package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@RestController
@RequestMapping("/library/menu")
public class MenuController {
    @Autowired private IMenuService imenuService;

    @GetMapping("/getMenuList")
    public ResultVO getMenuList(){
        ResultVO resultVO = this.imenuService.getMenuList();
        return resultVO;
    }
}

