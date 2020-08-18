package com.lxy.crm.service.impl;

import com.lxy.crm.domain.Menu;
import com.lxy.crm.mapper.MenuMapper;
import com.lxy.crm.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/15- 9:45
 */
@Service
public class MenuServiceImpl implements IMenuService {

    private final MenuMapper menuMapper;

    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    /**
     * 查询父子节点
     * @return List
     */
    @Override
    public List<Menu> queryForMenu() {
        return menuMapper.queryForMenu();
    }
}
