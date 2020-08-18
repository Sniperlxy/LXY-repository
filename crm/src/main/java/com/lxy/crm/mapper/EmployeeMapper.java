package com.lxy.crm.mapper;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.query.EmployeeQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/10- 23:18
 */
public interface EmployeeMapper {
    /*--------------下面两个方法是新增员工并且维护emp_role中间表------------------*/
    /*---------------1.新增员工信息-------------------------------------------*/
    /*---------------2.新增员工里有emp-role多对多的对应关系，需要用中间表emp_role来进行维护
    将对应的员工e_id,与角色 r_id插入中间表
    *
    * */
    /**
     * 新增员工
     * @param record 员工对象
     * @return 受影响的行数
     */
    int insert(Employee record);
    /**
     *新增员工同时维护emp_role中间表
     * @param rid 员工对应的角色id
     * @param eid 员工自身的id
     */
    void insertRelation(@Param("rid") Long rid, @Param("eid") Long eid);

    /*-----------------下面这个方法软删除员工---------------------*/
    /**
     * 软删除员工(改变员工状态为离职)
     * @param id 员工的id
     * @return 受影响的行数
     */
    int deleteByPrimaryKey(Long id);


    /*---------------下面两个方法是更新员工信息并且维护emp_role表-------------------*/
    /*---------------1.更新员工信息-------------------------------------------*/
    /*---------------2.更新员工里有emp-role多对多的对应关系，需要用中间表emp_role来进行维护
    将旧的->对应的员工e_id,与角色 r_id中间表关系删除*/
    /*---------------3.重新插入新的对应关系到emp_role表-----------------------*/
    /**
     * 更新员工信息
     * @param record 员工对象信息
     * @return 受影响的行数
     */
    int updateByPrimaryKey(Employee record);

    /**
     删除旧的对应关系
     * @param eid 对应的员工id
     */
    void deleteRelation(Long eid);

    /*---------------------下面这个方法是为了登录---------------------*/
    /**
     登录
     * @param username 用户名
     * @param password 密码
     * @return 查询到的员工记录
     */
    Employee getEmployeeForLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 切换用户状态
     * @param employee 当前登录用户
     *
     */
    void updateOnline(Employee employee);

    /*---------------------下面这个两个方法是高级分页+关键字查询----------------*/
    /**
     *查询员工总数+关键字搜索
     * @param  qo (3个属性:page->当前页,rows->当前页要显示的行数,keyword:关键字)
     * @return 返回查询到的员工总数
     */
    Long queryForPageCount(EmployeeQueryObject qo);

    /**
     * 分页查询所有员工+关键字搜索
     * @param  qo (3个属性:page->当前页,rows->当前页要显示的行数,keyword:关键字)
     * @return 返回查询到的员工的List集合
     */
    List<Employee> queryForPage(EmployeeQueryObject qo);

    /*-------------------下面这个方法是为了在员工管理页面(新增/编辑)回显员工所对应的角色信息--*/
    /**
     * 发送ajax同步请求
     * 根据每个员工的id查询对应所拥有的角色id集合
     * @param eid 员工对应的id
     * @return 返回该员工对应的角色id的集合
     */
    List<Long> queryByEid(long eid);



    /*----------------------------下面是超级管理员才能导出在职员工的excel表--------------*/
    /**
     * 查询所有在职员工导出为excel表
     * @return 返回装有在职员工部分信息封装得List集合
     */
    List<Employee> selectAllForExcel();
}