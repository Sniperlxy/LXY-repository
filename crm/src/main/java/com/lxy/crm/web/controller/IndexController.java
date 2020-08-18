package com.lxy.crm.web.controller;

import com.lxy.crm.domain.Menu;
import com.lxy.crm.util.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/11- 10:49
 */
@Controller
public class IndexController {

    /**
     * 请求首页(直接请求配置拦截器拦截
     * 重定向到login.jsp)
     * @return 首页地址
     */
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 返回父子节点
     * @return 所有菜单集合
     */
    @RequestMapping("/queryForMenu")
    @ResponseBody
     public List<Menu> queryForMenu(){
        //从Seesion域中取出所有的菜单集合
        return (List<Menu>) UserContext.get().getSession().getAttribute(UserContext.MENU_IN_SESSION);
    }
}

