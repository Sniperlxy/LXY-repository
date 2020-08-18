package com.lxy.crm.service.impl;

import com.lxy.crm.domain.Employee;
import com.lxy.crm.domain.Permission;
import com.lxy.crm.mapper.PermissionMapper;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.PermissionQueryObject;
import com.lxy.crm.query.QueryObject;
import com.lxy.crm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/13- 14:00
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    /***未做
     * 删除权限
     * @param id 权限id
     * @return 受影响行数
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增权限
     * @param record 权限对象
     * @return 受影响行数
     */
    @Override
    public int insert(Permission record) {
        return permissionMapper.insert(record);
    }

    /**
     * 未使用
     * @param id 权限
     * @return 权限对象
     */
    @Override
    public Permission selectByPrimaryKey(Long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }


    /**
     * 权限验证查询所有的权限信息
     * @return 所有权限对象的集合
     */
    @Override
    public List<Permission> selectAll() {
        return permissionMapper.selectAll();
    }

    /**未做
     * 更新权限
     * @param record 权限对象
     * @return 受影响行数
     */
    @Override
    public int updateByPrimaryKey(Permission record) {
        return permissionMapper.updateByPrimaryKey(record);
    }

    /**
     * 分页查询+关键字搜索
     * @param qo 自行查看
     * @return 分页对象
     */
    @Override
    public PageResult queryForPage(QueryObject qo) {
        //先查询总记录数
        Long total=permissionMapper.queryPageCount(qo);
        if (total==0){
            //说明没有记录,就直接返回pageresult对象
            return  new PageResult(0, Collections.EMPTY_LIST);
        }
        //如果有记录,就继续查询所有权限
        List<Permission> result=permissionMapper.queryPage(qo);
        return new PageResult(total.intValue(),result);
    }
    /**
     * 权限验证通过用户id查询用户所具有的权限表达式
     * 封装成一个List<String>集合
     * @param id 用户id
     * @return 所拥有的权限表达式集合
     */
    @Override
    public List<String> queryPermissionByEid(Long id) {
        return permissionMapper.queryPermissionByEid(id);
    }
}
