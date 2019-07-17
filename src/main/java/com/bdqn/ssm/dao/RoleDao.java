package com.bdqn.ssm.dao;

import com.bdqn.ssm.pojo.Role;

import java.util.List;

public interface RoleDao {

    /**
     * @Description:得到用户角色列表
     * @param: []
     * @return: List<Role>
     * @Date: 2019-07-05
     */
    public List<Role> getRoleLists();
}
