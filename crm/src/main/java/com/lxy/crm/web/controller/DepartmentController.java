package com.lxy.crm.web.controller;

import com.lxy.crm.domain.Department;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.DepartmentQueryObject;
import com.lxy.crm.service.IDepartmentService;
import com.lxy.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**由于外键关联所以部门管理只做新增和分页查询
 * @author LiuXiaoYu
 * @date 2020/8/12- 8:46
 */
@Controller
public class DepartmentController {

    private final IDepartmentService departmentService;

    @Autowired
    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 来到部门管理页面
     * @return 来到部门首页
     */
    @RequestMapping("/department")
    public String index() {
        return "department";
    }


    /**部门管理
     *新增部门
     * @param department 新增的部门对象
     * @return 返回Json对象
     */
    @RequestMapping("/dep_save")
    @ResponseBody
    public AjaxResult save(Department department) {
        AjaxResult result;
        try {
            departmentService.insert(department);
            result = new AjaxResult("保存成功", true);
        } catch (Exception e) {
            result = new AjaxResult("保存异常，请联系管理员", false);
        }
        return result;
    }

    /**更新部门(不想做了重复的开发模式)
     * @param department 更新的部门信息
     * @return 自定义的Json对象
     */
    @RequestMapping("/dep_update")
    @ResponseBody
    public AjaxResult update(Department department) {
        AjaxResult result;
        try {
            departmentService.updateByPrimaryKey(department);
            result = new AjaxResult("更新功能未开发", true);
        } catch (Exception e) {
            result = new AjaxResult("更新异常，请联系管理员", false);
        }
        return result;
    }

    /**不想做了重复的开发模式
     *删除部门
     * @param id 部门id
     * @return 自定义的Json对象
     */
    @RequestMapping("/dep_delete")
    @ResponseBody
    public AjaxResult delete(Long id) {

        AjaxResult result;
        try {
            departmentService.updateState(id);
            result = new AjaxResult("删除功能未开发", true);
        } catch (Exception e) {
            result = new AjaxResult("删除异常，请联系管理员", false);
        }
        return result;
    }

    /**
     * 部门管理
     * 分页+关键字查询展示所有部门信息
     * @param queryObject (请自行查看)
     * @return 自定义的分页工具类
     */
    @RequestMapping("/dep_list")
    @ResponseBody
    public PageResult list(DepartmentQueryObject queryObject) {
        return departmentService.queryForPage(queryObject);
    }


    /**员工管理页面
     * 所有部门在新增/编辑选择框里回显
     * @return 所有Department对象在选择框里回显
     */
    @RequestMapping("/department_queryForEmployee")
    @ResponseBody
    public List<Department> queryForEmp(){
        return departmentService.queryForEmp();
    }

}
