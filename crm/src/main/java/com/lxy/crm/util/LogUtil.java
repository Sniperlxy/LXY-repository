package com.lxy.crm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxy.crm.domain.Employee;
import com.lxy.crm.domain.Log;
import com.lxy.crm.service.ILogService;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**AOP日志(切面工具类)
 * @author LiuXiaoYu
 * @date 2020/8/12- 23:38
 */
public class LogUtil {

     @Autowired
      private ILogService logService;


    /**
     * AOP日志实现
     * @param joinPoint 切面对象
     */
    public void writeLog(JoinPoint joinPoint){
        //防止AOP切logService本身，造成死循环
        if (joinPoint.getTarget() instanceof ILogService){
            return;
        }
        //创建一个log对象
        Log log=new Log();
        //首先得到request当前线程的request
        HttpServletRequest request = UserContext.get();
        //得到当前使用用户
       Employee employee = (Employee) request.getSession().getAttribute(UserContext.USER_IN_SESSION);
        //得到当前用户设置给log
       log.setOpuser(employee);
        //设置时间
        log.setOptime(new Date());
        //设置ip地址
        log.setOpip(request.getRemoteAddr());
        //设置function
        log.setFunction(joinPoint.getTarget().getClass().getName()+":"+joinPoint.getSignature().getName());
        //设置params,将请求参数转换为json字符串
        ObjectMapper objectMapper=new ObjectMapper();
        String args;
        try {
            args=objectMapper.writeValueAsString(joinPoint.getArgs());
            log.setParams(args);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //最后插入日志到数据库
        logService.insert(log);

    }
}
