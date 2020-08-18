package com.lxy.crm.service;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.EmployeeQueryObject;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/10- 23:21
 */
public interface IEmployeeService {
    /**
     * 软删除员工
     * @param id 员工的id
     * @return 返回受影响的行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     *新增一条员工记录
     * @param record 新增的员工对象
     * @return 受影响的行数
     */
    int insert(Employee record);

    /**编辑用
     * 更新员工信息
     * @param record 更新员工信息
     * @return 受影响的行数
     */
    int updateByPrimaryKey(Employee record);

    /**
     * 登录时查询数据库是否有对应的用户
     * @param username 用户名
     * @param password 密码
     * @return 返回对应的员工
     */
    Employee getEmployeeForLogin(String username, String password);

    /**
     * 高级分页+关键字搜索
     * @param qo 自定义的工具类(包含关键字搜索的keyword+
     *           当前页面page+当前页要显示的行数rows)
     * @return 返回自定义的工具类
     */
    PageResult queryForPage(EmployeeQueryObject qo);

    /**
     * 发送ajax同步请求
     * 根据每个员工的id查询对应所拥有的角色id集合
     * @param eid 每个员工的id
     * @return 返回角色id的List集合
     */
    List<Long> queryByEid(long eid);

    /**
     * 更新登录员工状态,登录改为在线,退出时改为离线
     * 主要是防止二次登录
     * @param employee 登录时的员工
     */
    void updateOnline(Employee employee);

    /**
     *超级管理员导出在职员工部分信息
     * 为Excel查询用
     * @return 查询所有在职员工部分信息返回的List集合
     */
    List<Employee> selectAllForExcel();
}
