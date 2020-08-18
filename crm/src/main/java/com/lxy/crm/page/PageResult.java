package com.lxy.crm.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**自定义的返回json格式的对象
 * @author LiuXiaoYu
 * @date 2020/8/11- 22:33
 */
@Getter@Setter
public class PageResult {
    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 所有的对象信息(是个集合)
     */
    private List rows;

    public PageResult(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }
}
