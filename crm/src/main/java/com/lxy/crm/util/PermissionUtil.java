package com.lxy.crm.util;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.domain.Menu;
import com.lxy.crm.domain.Permission;
import com.lxy.crm.service.IPermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/14- 17:56
 */
@Component
public class PermissionUtil {

    private static IPermissionService permissionService;

    @Autowired
    public void setPermissionService(IPermissionService permissionService){
        PermissionUtil.permissionService=permissionService;
    }

    /**
     * url权限验证
     * @param function 一个包名+类名+方法名拼接的地址
     * @return true or false
     */
    public static boolean checkPermission(String function) {
        /*
         * 如果是超级管理员直接放行
         * 拿到当前系统所有权限资源（需要权限验证的url），判断当前方法是否包含在其中
         *   1.不包含：不需要权限判断，返回true，放行
         *   2.包含，则进一步判断当前用户是否拥有该权限
         *       1)拥有：返回true，放行
         *       2)没有：返回false，拦截
         *
         * */
        // 如果是超级管理员，直接放行
        Employee currentUser = (Employee) UserContext.get().getSession().getAttribute(UserContext.USER_IN_SESSION);
        if (currentUser.getAdmin()&& currentUser.getState()) {
            return true;
        }
        //当前系统所有需要权限检验的url
        if (CommonUtil.ALL_PERMISSIONS.size()==0) {
            //从数据库中查询所有的资源
            List<Permission> allPermissions = permissionService.selectAll();
            //遍历加到自定义的静态List<String>集合中
            for (Permission permission : allPermissions) {
                CommonUtil.ALL_PERMISSIONS.add(permission.getResource());
            }
        }
        //判断当前权限url是否在数据库中
        if (CommonUtil.ALL_PERMISSIONS.contains(function)){
            //包含，进一步判断当前用户是否拥有该权限
            //从Session域中获取用户所具有权限表达式集合
            List<String> currentUserPermission = (List<String>) UserContext.get().getSession().getAttribute(UserContext.PERMISSION_IN_SESSION);
            //判断当前用户的权限表达式包不包含当前权限url
            if (currentUserPermission.contains(function)){
                //包含直接放行
                return true;
            }else {
                //不包含
                //在判断是不是ALL权限
                String ALL=function.split(":")[0]+":ALL";
                //如果是ALL权限直接放行
                //如果也不是ALL权限
                return currentUserPermission.contains(ALL);
            }
        }else {
            return  true;
        }
    }

    /**
     * 菜单权限验证
     * @param menus 菜单集合
     */
    public static void checkMenuPermission(List<Menu> menus) {
        //从Seesion中获取到当前用户的权限集合
        List<String> userPermissions = (List<String>) UserContext.get().getSession().getAttribute(UserContext.PERMISSION_IN_SESSION);
        //遍历系统一级菜单与当前用户权限集合进行比对
        for (int i=0;i<menus.size();i++){
            String menuPermissions=menus.get(i).getFunction();
            //一级菜单也需要访问权限
            if (StringUtils.isNotBlank(menuPermissions)){
                //一级菜单需要权限
                //再判断用户权限是否包含这个一级菜单的权限
                if (!userPermissions.contains(menuPermissions)){
                    /*不包含，说明用户没有访问该一级菜单的权限，就删除它,
                    ，前台就不会显示该一级菜单*/
                    menus.remove(i);
                    // 注意，如果是从前往后遍历，一定要i--，会出错
                   i--;
                }
                //else 说明包含就不删除，跳过
            }
            //else说明一级菜单里的权限集合为空，那么就不做一级菜单权限验证，直接跳过一级菜单项

            //来到二级菜单，三级菜单。。。无线循环，符合递归思想,下面开始遍历n级菜单
            //递归遍历一级菜单下的所有子菜单

            //注意将i--后的i=-1恢复为0,不然报数组越界异常
            if (i==-1){
                i=0;
            }
            List<Menu> childernMenus=menus.get(i).getChildren();
            //判断子菜单是否为空,为空跳过，因为递归所以直接来不为空的
            if (!childernMenus.isEmpty()){
              checkMenuPermission(childernMenus);
            }

        }

    }

}
