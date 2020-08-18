package com.lxy.crm.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuXiaoYu
 * @date 2020/8/11- 22:40
 */
@Getter@Setter
public class EmployeeQueryObject extends QueryObject {
    /**关键字分页查询*/
    private String keyword;
    /**是否是超级管理员*/
    private static boolean admin;


    /**
     * 分页查询+关键字搜索用,给是否是超级管理员赋值
     * @param admin 超级管理员显示全部员工包括自己
     *  普通管理员不能看到超级管理员信息
     */
    public static void setAdmin(boolean admin) {
        EmployeeQueryObject.admin=admin;
    }


}
