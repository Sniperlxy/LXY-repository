package com.lxy.crm.service;

import com.lxy.crm.domain.Permission;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.QueryObject;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:58
 */
public interface IPermissionService {
    /**
     * 删除权限未做
     * @param id 权限id
     * @return 受影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新增权限
     * @param record 权限对象
     * @return 受影响行数
     */
    int insert(Permission record);

    /**
     * 未使用
     * @param id 权限
     * @return 权限对象
     */
    Permission selectByPrimaryKey(Long id);

    /**
     * 查找所有的权限信息
     * @return List集合
     */
    List<Permission> selectAll();

    /**
     * 更新权限
     * @param record 权限对象
     * @return 受影响行数
     */
    int updateByPrimaryKey(Permission record);

    /**
     * 分页查询+关键字搜索
     * @param qo 自行查看
     * @return 自定义的分页对象
     */
    PageResult queryForPage(QueryObject qo);

    /**
     * 权限验证通过用户id查询用户所具有的权限表达式
     * 封装成一个List<String>集合
     * @param id 员工Id
     * @return 用户所拥有的的权限表达式
     */
    List<String> queryPermissionByEid(Long id);
}
