package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Menu;
import com.nie.library.entity.MenuBack;
import com.nie.library.mapper.MenuBackMapper;
import com.nie.library.service.IMenuBackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
@Service
public class MenuBackServiceImpl extends ServiceImpl<MenuBackMapper, MenuBack> implements IMenuBackService {

    @Override
    public ResultVO getMenuBackList() {
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuBack::getStatus, "1");
        queryWrapper.orderByAsc(MenuBack::getMenuBackId);
        ResultVO resultVO = new ResultVO<>();
        List<MenuBack> allMenuBackList = this.list(queryWrapper); // 获取所有菜单数据
        Map<Integer , MenuBack> menuBackMap = new HashMap<>();
        Map<Integer , List<MenuBack>>  recodeMenuBackMap = new HashMap<>();
        for(MenuBack menuBack : allMenuBackList){
            menuBackMap.put(menuBack.getMenuBackId(),menuBack);
            Integer menuBackParentId = menuBack.getParentId();
            if(!recodeMenuBackMap.containsKey( menuBackParentId)){
                recodeMenuBackMap.put(menuBackParentId,new ArrayList<>());
            }
            recodeMenuBackMap.get(menuBackParentId).add(menuBack);
        }
        List<MenuBack> rootMenuBackList = recodeMenuBackMap.get(0);
        if(rootMenuBackList == null){
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
        }
        rootMenuBackList.sort(Comparator.comparing(MenuBack::getMenuBackId));
        for(MenuBack menuBack:rootMenuBackList){
            buildChildren(menuBack,recodeMenuBackMap);
        }
        resultVO.setCode(0);
        resultVO.setData(rootMenuBackList);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    private void buildChildren(MenuBack menuBack, Map<Integer,List<MenuBack>> recodeMenuBackMap){
        List<MenuBack> childrenMenuBackList = recodeMenuBackMap.get(menuBack.getMenuBackId());
        if(childrenMenuBackList != null && !childrenMenuBackList.isEmpty()){
            childrenMenuBackList.sort(Comparator.comparing(MenuBack::getMenuBackId));
            menuBack.setChildren(childrenMenuBackList);
            for(MenuBack childMenuBack : childrenMenuBackList){
                buildChildren(childMenuBack,recodeMenuBackMap);
            }
        }

    }
}
