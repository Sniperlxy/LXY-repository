package com.lxy.crm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lxy
 */

@Getter@Setter@ToString
public class Employee {
    /**职员自增id*/
    private Long id;

    /**职员账户名*/
    private String username;

    /**职员真实名字*/
    private String realname;

    /**职员密码*/
    private String password;

    /**职员电话号码*/
    private String tel;

     /**职员邮箱*/
    private String email;

    /**该职员的所属部门*/
    private Department dept;

    /**职员的入职时间*/
    @JsonFormat(pattern ="yyyy-MM-dd")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private Date inputtime;

    /**该职员状态(1为在职，0为离职)*/
    private Boolean state;

    /**是否是超级管理员(1是，0不是)*/
    private Boolean admin;

    /**是否在线*/
    private Boolean online;

    /**n-n关系:employee对应多个角色信息*/
    List<Role> roles=new ArrayList<>();

}