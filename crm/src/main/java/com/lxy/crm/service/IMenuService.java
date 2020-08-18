package com.lxy.crm.service;

import com.lxy.crm.domain.Menu;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/15- 9:44
 */
public interface IMenuService {
    /**
     * 查询父子节点
     * @return 返回遍历到的所有菜单集合
     */
    List<Menu> queryForMenu();
}
