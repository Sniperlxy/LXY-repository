package com.lxy.crm.mapper;

import com.lxy.crm.domain.Menu;
import java.util.List;

/**
 * @author LiuXiaoYu
 */
public interface MenuMapper {

    /**递归
     * 查询父子节点
     * @return 返回遍历到的所有菜单
     */
    List<Menu> queryForMenu();


}