package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.entity.MenuBack;
import com.nie.library.service.IMenuBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
@RestController
@RequestMapping("/library/menu-back")
public class MenuBackController {
    @Autowired private IMenuBackService imenuBackService;

    @GetMapping("/getMenuBackList")
    public ResultVO getMenuBackList(){
        ResultVO resultVO = this.imenuBackService.getMenuBackList();
        return resultVO;
    }
}

