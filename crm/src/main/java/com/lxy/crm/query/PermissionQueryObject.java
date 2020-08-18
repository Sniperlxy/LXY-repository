package com.lxy.crm.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:50
 */
@Getter@Setter
public class PermissionQueryObject extends QueryObject {

    //给出编辑页面通过指定id查询回显已拥有的权限
    private Long rid;
    //关键字分页查询
    private String keyword;

}
