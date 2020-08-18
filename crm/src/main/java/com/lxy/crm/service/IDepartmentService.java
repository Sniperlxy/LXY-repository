package com.lxy.crm.service;

import com.lxy.crm.domain.Department;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.QueryObject;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/12- 8:50
 */
public interface IDepartmentService {

    /**
     * 在员工管理页面回显部门
     * @return 返回查询到的所有部门
     */
    List<Department> queryForEmp();

    /**部门管理
     * 分页展示+关键字查询
     * @param queryObject (自行查看)
     * @return 自定义的分页工具类
     */
    PageResult queryForPage(QueryObject queryObject);

    /**
     * 新增部门
     * @param department 新增的部门对象
     */
    void insert(Department department);

    /**
     * 编辑部门
     * @param department 要编辑的部门对象
     */
    void updateByPrimaryKey(Department department);

    /**软删除部门
     * @param id 部门id
     */
    void updateState(Long id);
}
