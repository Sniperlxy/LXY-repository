package com.lxy.crm.web.controller;

import com.lxy.crm.domain.Permission;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.PermissionQueryObject;
import com.lxy.crm.service.IPermissionService;
import com.lxy.crm.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:13
 */
@Controller
public class PermissionController {

    private final IPermissionService permissionService;

    @Autowired
    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 权限管理首页
     * @return 地址
     */
    @RequestMapping("/permission")
    public String index() {
        return "permission";
    }


    /**权限管理页面
     *分页查询+关键字搜索
     * @param qo 自行查看
     * @return 自定义的分页工具类
     */
    @RequestMapping("/permission_list")
    @ResponseBody
    public PageResult list(PermissionQueryObject qo){
        PageResult pageResult;
        pageResult = permissionService.queryForPage(qo);
        return pageResult;
    }

    /**角色管理页面
     * 角色管理中回显该角色所拥有的的权限
     * @param qo 自行查看
     * @return 自定义的分页工具类对象
     */
    @RequestMapping("/permission_queryByRid")
    @ResponseBody
    public PageResult queryByRid(PermissionQueryObject qo){
        PageResult pageResult;
        pageResult = permissionService.queryForPage(qo);
        return pageResult;
    }

    /**
     * 保存权限
     * @param permission 权限对象
     * @return 自定义的Json对象
     */
    @RequestMapping("/permission_save")
    @ResponseBody
    public AjaxResult save(Permission permission) {

        AjaxResult result;
        try {
            permissionService.insert(permission);
            result = new AjaxResult("保存成功", true);
        } catch (Exception e) {
            result = new AjaxResult("保存异常，请联系管理员", false);
        }
        return result;
    }

    /**
     * 更新权限未做
     * @param permission 要更新的权限对象
     * @return Json对象
     */
    @RequestMapping("/permission_update")
    @ResponseBody
    public AjaxResult update(Permission permission) {
        AjaxResult result;
        try {
            permissionService.updateByPrimaryKey(permission);
            result = new AjaxResult("更新成功", true);
        } catch (Exception e) {
            result = new AjaxResult("更新异常，请联系管理员", false);
        }
        return result;
    }

    /**未做
     * 删除权限
     * @param  id 权限id
     * @return Json对象
     */
    @RequestMapping("/permission_delete")
    @ResponseBody
    public AjaxResult delete(Long id) {
        AjaxResult result;
        try {
            permissionService.deleteByPrimaryKey(id);
            result = new AjaxResult("删除功能未开发", true);
        } catch (Exception e) {
            result = new AjaxResult("删除失败，请联系管理员", false);
        }
        return result;
    }
}
