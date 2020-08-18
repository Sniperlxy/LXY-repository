package com.lxy.crm.mapper;

import com.lxy.crm.domain.Department;
import com.lxy.crm.query.QueryObject;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/12 8:47
 */
public interface DepartmentMapper {
    /**
     * 软删除部门
     * @param id 部门id
     * @return 受影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**部门管理
     * 新增部门
     * @param record 新增的部门对象
     * @return 受影响行数
     */
    int insert(Department record);


    /**
     * 更新部门
     * @param record 要编辑的部门对象
     * @return 受影响行数
     */
    int updateByPrimaryKey(Department record);

    /**在员工管理的新增/编辑页面回显部门
     * 查询所有的部门
     * @return 部门集合
     */
    List<Department> queryForEmp();

    /*----------------------------------下面两个方法是分页查询+关键字搜索----------------*/
    /**
     * 查询记录总数+关键字查询
     * @param qo 自行查看
     * @return 总数
     */
    Long queryPageCount(QueryObject qo);

    /**
     * 查询记录总数+关键字查询
     * @param qo 自行查看
     * @return  部门集合
     */
    List<Department> queryPage(QueryObject qo);
}