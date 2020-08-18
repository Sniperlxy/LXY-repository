package com.lxy.crm.query;

import lombok.Getter;
import lombok.Setter;

/**提取公共的分页信息让子类继承
 * @author LiuXiaoYu
 * @date 2020/8/13- 13:49
 */
@Getter@Setter
public class QueryObject {

    /**当前页的参数*/
    private Integer page;
    /**一页显示几行的参数*/
    private Integer rows;


    /**
     * (当前页-1)*每页记录数
     * @return (当前页-1)*每页记录数
     */
    public Integer getStart(){
        return (this.page-1)*this.rows;
    }
}
