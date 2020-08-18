package com.lxy.crm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author LiuXiaoYu
 */
@Getter
@Setter
@ToString
public class Department {
    /**部门自增id*/
    private Long id;

    /**部门编号*/
    private String sn;

    /**部门名称*/
    private String name;

    /**部门经理*/
    private Employee manager;

    /**上级部门*/
    private Department parent;

    /**部门状态(1为此部门存在，0为此部门不存在了)*/
    private Boolean state;
}