package com.lxy.crm.service.impl;

import com.lxy.crm.domain.Department;
import com.lxy.crm.mapper.DepartmentMapper;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.QueryObject;
import com.lxy.crm.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**部门管理Service层
 * @author LiuXiaoYu
 * @date 2020/8/12- 8:50
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**service层
     * 所有部门在选择框里回显
     * @return 所有Department对象在选择框里回显
     */
    @Override
    public List<Department> queryForEmp() {
        return departmentMapper.queryForEmp();
    }


    /**service层
     * 分页查询所有员工
     * @param  qo (两个属性:page->当前页,rows->当前页要显示的行数)
     * @return PageResult(两个属性:total-查询到的总记录数>
     * rows->查询到所有员工的List集合)
     */
    @Override
    public PageResult queryForPage(QueryObject qo) {
        //先查询总记录数
        Long total= departmentMapper.queryPageCount(qo);
        if (total==0){
            //说明没有记录,就直接返回pageresult对象
            return  new PageResult(0, Collections.EMPTY_LIST);
        }
        //如果有记录,就继续查询所有部门
        List<Department> result=departmentMapper.queryPage(qo);
        return new PageResult(total.intValue(),result);
    }

    /**部门管理
     * 新增部门
     * @param department 新增的部门对象
     */
    @Override
    public void insert(Department department) {
        departmentMapper.insert(department);
    }

    /**没做
     * 更改部门信息
     * @param department 要编辑的部门对象
     */
    @Override
    public void updateByPrimaryKey(Department department) {

    }

    /**
     * 软删除部门 没做
     * @param id 部门id
     */
    @Override
    public void updateState(Long id) {

    }
}
