package com.lxy.crm.service.impl;

import com.lxy.crm.domain.Permission;
import com.lxy.crm.domain.Role;
import com.lxy.crm.mapper.RoleMapper;
import com.lxy.crm.page.PageResult;
import com.lxy.crm.query.QueryObject;
import com.lxy.crm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**角色管理Service层
 * @author LiuXiaoYu
 * @date 2020/8/13- 19:07
 */
@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**直接根据传入的角色id删除不行，还得维护中间表
     * role_permission表中该角色所对应的权限也要删除
     * emp_role中该角色对应的
     * @param  id 传入的角色id
     * @return 返回受影响的行数
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        /*注意这里首先得根据rid删除role_permission的信息，
        不然直接返回失败，连没有员工对应的角色都删除不了*/
        roleMapper.deletePermissionByRid(id);
        // 再删除删除role信息
        return roleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 插入一条新增的角色信息,同时处理多对对关系的中间表role_permission
     * @param record (Role对象)
     * @return int 影响的行数
     */
    @Override
    public int insert(Role record) {
        //只处理插入信息中的 sn(编号),name(部门名称)
        int effectCount = roleMapper.insert(record);
        //还得处理角色管理页面已选的权限信息(用List装载的permissions)
        for(Permission permission:record.getPermissions()){
            //处理中间表方法
            roleMapper.insertRelation(record.getId(),permission.getId());
        }
        return  effectCount;
    }

    @Override
    public Role selectByPrimaryKey(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Role> selectAll() {
        return roleMapper.selectAll();
    }

    /**
     * 编辑角色更新角色信息并且更新权限
     * @param record 角色
     * @return 受影响行数
     */
    @Override
    public int updateByPrimaryKey(Role record) {
        // 更新role表
        int effectedCount = roleMapper.updateByPrimaryKey(record);
        // 根据rid删除role_permission中该角色原有权限（旧）
        roleMapper.deletePermissionByRid(record.getId());
        // 把角色拥有的权限（新）插入role_permission，以此关联角色和权限
        for (Permission permission : record.getPermissions()) {
            roleMapper.insertRelation(record.getId(), permission.getId());
        }
        return effectedCount;
    }


    /**service层
     * 分页查询所有员工
     * @param  qo (两个属性:page->当前页,rows->当前页要显示的行数)
     * @return PageResult(两个属性:total-查询到的总记录数>
     * rows->查询到所有员工的List集合)
     */
    @Override
    public PageResult queryForPage(QueryObject qo) {
        //先查询总记录数
        Long total=roleMapper.queryPageCount(qo);
        if (total==0){
            //说明没有记录,就直接返回pageresult对象
            return  new PageResult(0, Collections.EMPTY_LIST);
        }
        //如果有记录,就继续查询所有员工
        List<Role> result=roleMapper.queryPage(qo);
        return new PageResult(total.intValue(),result);
    }

    /**
     * 为了在员工管理新增框/编辑框里回显角色
     * @return 所有角色对象
     */
    @Override
    public List<Role> queryForEmployeeRole() {
        return roleMapper.queryForEmployeeRole();
    }




}
