package com.lxy.crm.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuXiaoYu
 */
@Setter@Getter
public class Role {
    /**角色自增id*/
    private Long id;

    /**角色编号*/
    private String sn;

    /**角色名称*/
    private String name;

    /**用一个List装载permission(所有权限)*/
    private List<Permission> permissions=new ArrayList<>();
}