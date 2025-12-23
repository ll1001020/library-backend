package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Menu;
import com.nie.library.form.PaginationForm;
import com.nie.library.mapper.MenuMapper;
import com.nie.library.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
        @Autowired private MenuMapper menuMapper;

    @Override
    public ResultVO getMenuList() {
        // 1.获取所有菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(Menu::getStatus, "1");
        queryWrapper.orderByAsc(Menu::getMenuId);
        List<Menu> allMenuList = this.list(queryWrapper); // 成功获取到所有菜单数据储存到allMenuList中
        Map<Integer,Menu> menuMap = new HashMap<>();  // 创建一个字典menuMap，用于存储菜单数据，键为id，值为Menu对象
        Map<Integer,List<Menu>> recordMap = new HashMap<>();  // 创建一个字典recordMap，用于存储菜单数据，键为父id，值为储存Menu的List<Menu>对象
        // 2.分别储存菜单数据和父子关系到menuMap和recordMap中
        for(Menu menu: allMenuList){  // 循环，每一次循环都从所有菜单数据中取出一个Menu对象
            menuMap.put(menu.getMenuId(), menu); // 将Menu对象存储到menuMap中，键为menuId，值为Menu对象
//            System.out.println(menuMap.get(menu.getMenuId()));
            Integer parentId = menu.getParentId();  // 获取当前Menu对象的父id到parentId中
            if (!recordMap.containsKey(parentId)){  // 如果当前recordMap中不存在当前Menu对象的父id，则创建一个空的List<Menu>对象同这个父id储存起来
                recordMap.put(parentId, new ArrayList<>());
            }
            recordMap.get(parentId).add(menu); // 将当前Menu对象添加到recordMap中，键为当前对象的父id
        }
        // 3.构建根菜单rootMenuList并调用buildChildren方法构建菜单树结构
        List<Menu> rootMenuList = recordMap.get(0);  // 从recordMap中取出父id为0的菜单数据
        if (rootMenuList == null){
            resultVO.setCode(-1);  // 这里设置-1同前端统一处理，表示没有菜单数据
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
            return resultVO;
        }
        rootMenuList.sort(Comparator.comparing(Menu::getMenuId));  // 对取出的一级菜单进行排序根据menuId
        for(Menu menu1: rootMenuList){
            buildChildren(menu1, recordMap);
        }
        resultVO.setCode(0);  // 这里设置0同前端统一处理，表示成功获取菜单数据
        resultVO.setData(rootMenuList);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    private void buildChildren(Menu menu, Map<Integer, List<Menu>> recordMap) {
        List<Menu> children = recordMap.get(menu.getMenuId());  // 获取当前菜单的子菜单数据
        if (children != null && !children.isEmpty()) {  // 如果子菜单不为空
            children.sort(Comparator.comparing(Menu::getMenuId));  // 将取出子菜单根据menuId排序
            menu.setChildren(children);  // 将储存子菜单的列表设置为当前菜单对象的children属性
            for (Menu child : children) {  // 递归设置子菜单的子菜单
                buildChildren(child, recordMap);
            }
        }
    }
}
