package com.bdqn.ssm.service;

import com.bdqn.ssm.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * @Description:得到用户角色列表
     * @param: []
     * @return: java.util.List<com.bdqn.ssm.pojo.Role>
     * @Date: 2019-07-05
     */
    public List<Role> getRoleList();
}
