package com.lxy.crm.service;

import com.lxy.crm.domain.Log;

import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/13- 9:17
 */
public interface ILogService {
    int deleteByPrimaryKey(Long id);

    int insert(Log record);

    Log selectByPrimaryKey(Long id);

    List<Log> selectAll();

    int updateByPrimaryKey(Log record);
}
