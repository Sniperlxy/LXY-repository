package com.lxy.crm.service.impl;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.domain.Role;
import com.lxy.crm.mapper.EmployeeMapper;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.EmployeeQueryObject;
import com.lxy.crm.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/10- 23:22
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     *软删除员工
     * @param id 员工的id
     * @return 返回受影响的行数
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增员工时不仅需要插入employee表
     * 还需要处理emp_role中间表关系
     * 因为员工与角色 是一个多对多的对应关系
     * @param record 新增的员工
     * @return 受影响的行数
     */
    @Override
    public int insert(Employee record) {
        //新增员工
        int effectCount = employeeMapper.insert(record);
        /*
        维护role_emp中间表,将rid,eid插入此表
        遍历所有的List<Role>得到id
        */
        for (Role role:record.getRoles()){
            employeeMapper.insertRelation(role.getId(),record.getId());
        }
        return effectCount;
    }

    /**
     * 超级管理员查询
     * 在职员工部分信息导出为Excel表
     * @return 所有要导出为Excel表的部分员工信息
     * 封装成一个Employee的List集合
     */
    @Override
    public List<Employee> selectAllForExcel() {
        return employeeMapper.selectAllForExcel();
    }

    /**
     *更新员工，
     * 并且还得维护emp_role中间表(多对多对应关系)，
     * 将对应的角色信息(旧)删除,重新插入新的角色信息到中间表(emp_role)
     * @param record 要更新的员工
     * @return 受影响的行数
     */
    @Override
    public int updateByPrimaryKey(Employee record) {
        //更新员工
        int effectCount = employeeMapper.updateByPrimaryKey(record);
        /*根据员工id
        维护role_emp中间表,将该员工所对应的角色信息(旧)删除
        */
        employeeMapper.deleteRelation(record.getId());

        /*
        重新插入新的角色信息到中间表
         */
        for (Role role:record.getRoles()){
            employeeMapper.insertRelation(role.getId(),record.getId());
        }
        return effectCount;
    }

    /**service层
     * 根据账号和密码查找用户
     * @param username 账号
     * @param password 密码
     * @return 调用dao层方法getEmployeeForLogin
     */
    @Override
    public Employee getEmployeeForLogin(String username, String password) {
        return employeeMapper.getEmployeeForLogin(username,password);
    }

    /**service层
     * 分页查询所有员工
     * @param  qo (两个属性:page->当前页,rows->当前页要显示的行数)
     * @return PageResult(两个属性:total-查询到的总记录数>
     * rows->查询到所有员工的List集合)
     */
    @Override
    public PageResult queryForPage(EmployeeQueryObject qo) {
        //先查询总记录数
        Long total=employeeMapper.queryForPageCount(qo);
        if (total==0){
            //说明没有记录,就直接返回pageresult对象
            return  new PageResult(0, Collections.EMPTY_LIST);
        }
        //如果有记录,就继续查询所有员工
        List<Employee> result=employeeMapper.queryForPage(qo);
        return new PageResult(total.intValue(),result);
    }

    /**
     * 发送ajax同步请求
     * 根据每个员工的id查询对应所拥有的角色id集合
     * @param eid 员工id
     * @return List<Long> 装有所有角色id的List集合
     */
    @Override
    public List<Long> queryByEid(long eid) {
        return employeeMapper.queryByEid(eid);
    }

    /**
     * 更改用户为在线状态
     * @param employee 当前登录用户的员工信息
     */
    @Override
    public void updateOnline(Employee employee) {
        employeeMapper.updateOnline(employee);
    }
}

