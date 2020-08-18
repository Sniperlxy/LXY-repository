package com.lxy.crm.web.controller;

import com.lxy.crm.domain.Role;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.RoleQueryObject;
import com.lxy.crm.service.IRoleService;
import com.lxy.crm.util.AjaxResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**角色管理Controller层
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:13
 */
@Controller
public class RoleController {

    private final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**来到角色管理页面
     * @return role
     */
    @RequestMapping("/role")
     public String index(){
        return "role";
    }


    /**角色管理页面
     * 分页展示所有角色
     * @param  qo (两个属性:page->当前页,rows->当前页要显示的行数)
     * @return PageResult(两个属性:total-查询到的总记录数>
     * rows->查询到所有员工的List集合)
     */
    @RequestMapping("/role_list")
    @ResponseBody
    public PageResult list(RoleQueryObject qo){
        PageResult result;
        result=roleService.queryForPage(qo);
        return result;
    }


    /**对角色管理页面的新增进行操作
     * 新增角色保存方法
     * @ return 返回自定义的json字符串,
     * 判断新增角色是否成功保存
     */
    @RequestMapping("/role_save")
    @ResponseBody
    public AjaxResult save(Role role){
        AjaxResult result;
        try {
            roleService.insert(role);
            //保存成功信息到自定义对象result
            result=new AjaxResult("新增角色成功",true);
        }catch (Exception e){
            result=new AjaxResult("新增角色失败,请联系管理员",false);
        }

        return  result;
    }


    /**对角色管理页面的编辑进行操作
     * 编辑角色(包括权限信息)并保存方法
     * @ return 返回自定义的json字符串,
     * 判断新增员工是否成功保存
     */
    @RequestMapping("/role_update")
    @ResponseBody
    public AjaxResult update(Role role){
        AjaxResult result;
        try {
            //调用更新方法
            roleService.updateByPrimaryKey(role);
            //保存成功信息到自定义对象result
            result=new AjaxResult("更新成功",true);
        }catch (Exception e){
            result=new AjaxResult("更新失败,请联系管理员",false);
        }

        return  result;
    }

    /**对角色管理页面的删除进行操作
     * 重点:只能删除没有员工对应的角色(因为中间表关系不好处理)
     * @param id 传入的角色id
     * @return 返回自定义的json字符串
     */
    @RequestMapping("/role_delete")
    @ResponseBody
    public AjaxResult delete(@Param("id") Long id) {
        AjaxResult result;
        try {
            roleService.deleteByPrimaryKey(id);
            result = new AjaxResult("删除成功", true);
        } catch (Exception e) {
            result = new AjaxResult("请先确认员工中没有该角色再点击删除", false);
        }
        return result;
    }

    /**对员工管理页面中新增/编辑框里的回显角色信息的操作
     * 所有角色在员工管理新增/编辑框里回显
     * @return 所有Role对象在选择框里回显
     */
    @RequestMapping("/role_queryForEmployeeRole")
    @ResponseBody
    public List<Role> queryForEmpRole(){
        return roleService.queryForEmployeeRole();
    }

}
