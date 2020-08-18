package com.lxy.crm.mapper;

import com.lxy.crm.domain.Log;
import java.util.List;

/**
 * @author LiuXiaoYu
 */
public interface LogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Log record);

    Log selectByPrimaryKey(Long id);

    List<Log> selectAll();

    int updateByPrimaryKey(Log record);
}