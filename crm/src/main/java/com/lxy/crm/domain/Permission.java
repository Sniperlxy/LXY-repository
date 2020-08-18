package com.lxy.crm.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuXiaoYu
 */
@Setter@Getter
public class Permission {
    /**权限自增id*/
    private Long id;

    /**权限名称*/
    private String name;

    /**权限拼接的路径(包名+类名+方法名)*/
    private String resource;
}