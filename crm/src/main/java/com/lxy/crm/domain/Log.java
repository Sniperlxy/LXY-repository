package com.lxy.crm.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author  LiuXiaoYu
 */
@Getter@Setter
public class Log {
    /**日志自增id*/
    private Long id;

    /**记录的员工*/
    private Employee opuser;

    /**时间*/
    private Date optime;

    /**ip地址*/
    private String opip;

    /**记录该用户登录后的操作,后台记录调用了Service层的哪些方法*/
    private String function;

    /**操作过来,记录参数信息*/
    private String params;
}