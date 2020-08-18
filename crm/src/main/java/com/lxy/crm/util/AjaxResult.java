package com.lxy.crm.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuXiaoYu
 * @date 2020/8/11- 19:24
 */
@Getter@Setter
public class AjaxResult {
    /**返回给前端的msg*/
    private String msg;

    /**登录请求是否成功*/
    private boolean success;

    /**
     *构造器
     * @param msg 返回Json的msg
     * @param success 返回Json的success
     */
    public AjaxResult(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }
}
