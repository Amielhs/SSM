package com.bdqn.ssm.service.impl;

import com.bdqn.ssm.dao.UserDao;
import com.bdqn.ssm.pojo.User;
import com.bdqn.ssm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Description:用户业务实现类
 * @Author: amielhs
 * @Date 2019-07-04
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Resource //默认按类型匹配或者使用@Autowired
    private UserDao userDao;

    /**
     * @Description:通过uId查找用户
     * @param: [uId]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-04
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.DEFAULT, readOnly = false, timeout = -1)
    public User findUserById(String uId) {
        return userDao.selectUserById(uId);
    }

    /**
     * @Description:用户登录
     * @param: [userCode, userPassword]
     * @return: com.bdqn.ssm.pojo.boolean
     * @Date: 2019-07-04
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User login(String userCode, String userPassword) {
        return userDao.getLoginUser(userCode,userPassword);
    }

    /**
     * @Description:根据userCode查询出User
     * @param: [userCode]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User selectUserCodeExist(String userCode) {
        return userDao.getLoginUserOnly(userCode);
    }

    /**
     * @Description:根据条件查询用户表记录数
     * @param: [queryUserName, queryUserRole]
     * @return: int
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer getUserCounts(String queryUserName, int queryUserRole) {
        return userDao.getUserCount(queryUserName,queryUserRole);
    }

    /**
     * @Description:增加用户信息
     * @param: [user]
     * @return: boolean
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean add(User user) {
        boolean flag = false;
        if (userDao.add(user)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:修改用户信息
     * @param: [user]
     * @return: boolean
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean modify(User user) {
        boolean flag = false;
        if (userDao.modify(user)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:根据userId修改密码
     * @param: [id, pwd]
     * @return: boolean
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updatePwd(int id, String pwd) {
        boolean flag = false;
        if (userDao.updatePwd(id,pwd)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:根据ID删除user
     * @param: [delId]
     * @return: boolean
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteUserById(String delId) {
        boolean flag = false;
        if (userDao.deleteUserById(delId)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:通过条件查询-userList
     * @param: [queryUserName, queryUserRole, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.User>
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        return userDao.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
    }

}
