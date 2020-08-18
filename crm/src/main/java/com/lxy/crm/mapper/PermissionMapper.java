package com.lxy.crm.mapper;

import com.lxy.crm.domain.Permission;
import com.lxy.crm.query.QueryObject;

import java.util.List;

/**
 * @author LiuXiaoYu
 *
 */
public interface PermissionMapper {
    /**
     * 未做
     * 删除权限
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
     * @param id 权限id
     * @return 权限对象
     */
    Permission selectByPrimaryKey(Long id);

    /**
     * 查询所有的权限
     * @return 权限集合
     */
    List<Permission> selectAll();

    /**未做
     * 更新权限
     * @param record 权限对象
     * @return 受影响行数
     */
    int updateByPrimaryKey(Permission record);

    /*----------------------下面这两个方法是分页查询+关键字搜索--------------------*/
    /**
     * 这个方法是分页查询的
     * @param qo 自行查看
     * @return 权限对象
     */
    List<Permission> queryPage(QueryObject qo);

    /**
     * 这个方法是查询总记录total的
     * @param qo 自行查看
     * @return 总数
     */
    Long queryPageCount(QueryObject qo);

    /**
     * 权限验证通过用户id查询用户所具有的权限表达式
     * 封装成一个List<String>集合
     * @param id 用户id
     * @return 权限表达式集合
     */
    List<String> queryPermissionByEid(Long id);
}