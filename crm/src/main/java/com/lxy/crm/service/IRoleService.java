package com.lxy.crm.service;

import com.lxy.crm.domain.Role;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.QueryObject;

import java.util.List;

/**Service接口
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:58
 */
public interface IRoleService {
    /**
     * 对角色管理页面的删除进行操作
     * @param  id 传入的角色id
     * @return 影响的行数
     */
    int deleteByPrimaryKey(Long id);

    /**对角色管理页面的新增进行操作
     * 新增角色保存方法
     * @param record 角色对象
     * @return 影响的行数
     */
    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    /**对角色管理页面的编辑进行操作
     * 编辑角色(包括权限信息)并保存方法
     * @param record 传入的角色对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(Role record);

    /**
     * 分页查询+关键字
     * @param queryObject 自定义的分页查询对象
     * @return 自定义的结果页(total,rows)
     */
    PageResult queryForPage(QueryObject queryObject);

    /**
     * 为了在员工管理新增框/编辑框里回显对应的角色
     * @return 一个具有role对象的list集合
     */
    List<Role> queryForEmployeeRole();


}

