package com.lxy.crm.web.interceptor;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.util.PermissionUtil;
import com.lxy.crm.util.UserContext;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;

/**记得去springmvc容器中注册拦截器
 * @author LiuXiaoYu
 * @date 2020/8/11- 19:45
 */
public class LoginInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //--------给当前线程传入request
        UserContext.set(request);

        //-----------------登录拦截-------------------
        // 从session中获取用户信息
        Employee employee = (Employee) request.getSession().getAttribute(UserContext.USER_IN_SESSION);
        if (employee == null) {
            //清除当前线程里的reuqest
            UserContext.remove();
            // 拦截请求并重定向到登录页面
             response.sendRedirect("/login.jsp");
            // 记得return false，否则程序还是会往下走，经过拦截器达到目标Controller
            return false;
        }

        /*-------------------------权限验证开始-----------------------*/
       //判断是否是HandlerMethod类型
        if(handler instanceof HandlerMethod){
            //把handler强转为MethodHandle
            HandlerMethod handlerMethod=(HandlerMethod)handler;
            //拼接字符串得到url权限验证的字符串
            String function=handlerMethod.getBean().getClass().getName()+":"+handlerMethod.getMethod().getName();
            //判断当前用户是否有权限访问
            boolean flag= PermissionUtil.checkPermission(function);
            if (flag){
                //有权限访问直接放行
                return true;
            }else {
                //没有权限访问
                //是否是ajax请求
                if (handlerMethod.getMethod().isAnnotationPresent(ResponseBody.class)){
                       //如果是ajax请求,返回失败信息即json对象
                    response.sendRedirect("/noPermission.json");
                }else {
                    //如果是页面
                    //如果是页面，重定向到noPermission.html
                    response.sendRedirect("/noPermission.html");
                }
                return false;
            }
        }
        return true;
    }

    @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {
                     //移除当前线程的request
                     UserContext.remove();

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {
    }
}
