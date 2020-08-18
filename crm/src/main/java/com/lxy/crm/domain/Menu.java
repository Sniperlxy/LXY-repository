package com.lxy.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**JsonIgnoreProperties注解来处理
 * Bean中属性序列化时抛出的异常
 * @author LiuXiaoYu
 */
@Setter@Getter
@JsonIgnoreProperties(value = {"handler"})
public class Menu implements Serializable {

    /**菜单的自增id*/
    private Long id;

    /**菜单名*/
    private String text;

    /**菜单名前面的小图标*/
    private String iconcls;

    /**菜单是否已被选中*/
    private Boolean checked;

    /**菜单节点状态*/
    private String state;

    /**
        当菜单展示在页面后，点击该菜单发出的请求url
     */
    private String attributes;

    /**
     数据库虽然是parent_id，但那只是为了表征父节点
      实际字段要看xml中sql是怎么查的，以及前台需要什么格式
     */
    private List<Menu> children;

    /**
     * 菜单权限验证
     */
    private String function;

}