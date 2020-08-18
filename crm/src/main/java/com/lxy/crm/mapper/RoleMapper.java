package com.lxy.crm.mapper;

import com.lxy.crm.domain.Role;
import com.lxy.crm.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiuXiaoYu
 */
public interface RoleMapper {

    /*----------下面两个方法是新增新的角色 一个是新增角色信息，
    另一个是插入中间表emp_permission所对应的权限信息(插入中间表方法新增和编辑两处都会调用)------------------*/
    /**
     * 新增角色信息
     * @param record 角色对象
     * @return 受影响行数
     */
    int insert(Role record);

    /**新增同时插入中间表的对应关系
     * 该方法会在编辑角色和新增角色模块被调用
     * @param rId 中间表的角色id
     * @param pId  中间表的权限id
     */
    void insertRelation(@Param("r_id") long rId, @Param("p_id") long pId);

     /*------------下面这两个方法是删除并且删除角色对应的权限信息,只能删除
    没有员工对应的角色，如果已有员工对应这个角色则不能删除------------------------*/
    /**
     * 根据rid删除role_permission中该角色原有权限（旧）
     * @param id 角色id
     */
    void deletePermissionByRid(Long id);
    /**
     * 删除对应的角色信息
     * @param id 角色id
     * @return 受影响行数
     */
    int deleteByPrimaryKey(Long id);

    /*---------三步操作 1.编辑角色信息 2.删除旧权限 3.插入新权限----------------------------------*/
    /**
     *编辑角色信息并且删除旧的权限，插入新的权限
     * @param record 角色对象
     * @return 受影响行数
     */
    int updateByPrimaryKey(Role record);


    /*---------------------高级分页查询+关键字搜索------------------------------------*/
    /**
     * 分页查询+关键字
     * @param qo 自行查看
     * @return 返回 rows rows里面则是一个List<Role>集合
     */
    List<Role> queryPage(QueryObject qo);
    /**
     * 分页查询+关键字
     * @param qo 自定义查询对象
     * @return 返回总数total
     */
    Long queryPageCount(QueryObject qo);


    /*---------------------员工管理新增/编辑框里回显角色-----------------------------------*/
    /**员工管理界面
     * 为了在员工管理界面新增框/编辑框回显角色
     * @return 所以角色对象
     */
    List<Role> queryForEmployeeRole();


    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();


}