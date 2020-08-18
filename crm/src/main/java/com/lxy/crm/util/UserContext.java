package com.lxy.crm.util;

import com.lxy.crm.domain.Log;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiuXiaoYu
 * @date 2020/8/11- 19:30
 */
public class UserContext {
    /**Session域中的Employee*/
    public static final String USER_IN_SESSION = "USER_IN_SESSION";

    /**Session域中的String(用户所具有的权限表达式集合)*/
    public static final String PERMISSION_IN_SESSION="PERMISSION_IN_SESSION";

    /**Session域的菜单集合*/
    public static final String MENU_IN_SESSION="MENU_IN_SESSION";

    /**私有一个Threadlocal以当前线程的ThreadLocal作为键,HttpServletRequest作为值*/
    private static final ThreadLocal<HttpServletRequest> LOCAL=new ThreadLocal<>();

    /**
     * 给出静态get和set方法
     *
     */
    public static void set(HttpServletRequest request){
        LOCAL.set(request);
    }

    public static HttpServletRequest get(){
        return LOCAL.get();
    }

    /**
     * 至少调用一次remove方法
     */
    public static void remove(){
        LOCAL.remove();
    }
}
