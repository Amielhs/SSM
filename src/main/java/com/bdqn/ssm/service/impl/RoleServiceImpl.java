package com.bdqn.ssm.service.impl;

import com.bdqn.ssm.dao.RoleDao;
import com.bdqn.ssm.pojo.Role;
import com.bdqn.ssm.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;
    /**
     * @Description:得到用户角色列表
     * @param: []
     * @return: java.util.List<com.bdqn.ssm.pojo.Role>
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.DEFAULT, readOnly = false, timeout = -1)
    public List<Role> getRoleList() {
        return roleDao.getRoleLists();
    }
}
