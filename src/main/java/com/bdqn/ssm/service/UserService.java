package com.bdqn.ssm.service;

import com.bdqn.ssm.pojo.User;

import java.util.List;

/**
 * @ClassName: UserService
 * @Description:用户业务接口
 * @Author: amielhs
 * @Date 2019-07-04
 */
public interface UserService {
    /**
     * @Description:通过uId查找用户
     * @param: [uId]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-04
     */
    User findUserById(String uId);

    /**
     * @Description:用户登录
     * @param: [userCode, userPassword]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-04
     */
    public User login(String userCode,String userPassword);

    /**
     * @Description:根据userCode查询出User
     * @param: [userCode]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-05
     */
    public User selectUserCodeExist(String userCode);

    /**
     * @Description:根据条件查询用户表记录数
     * @param: [queryUserName, queryUserRole]
     * @return: int
     * @Date: 2019-07-05
     */
    public Integer getUserCounts(String queryUserName,int queryUserRole);

    /**
     * @Description:增加用户信息
     * @param: [user]
     * @return: boolean
     * @Date: 2019-07-05
     */
    public boolean add(User user);

    /**
     * @Description:修改用户信息
     * @param: [user]
     * @return: boolean
     * @Date: 2019-07-05
     */
    public boolean modify(User user);

    /**
     * @Description:根据userId修改密码
     * @param: [id, pwd]
     * @return: boolean
     * @Date: 2019-07-05
     */
    public boolean updatePwd(int id,String pwd);

    /**
     * @Description:根据ID删除user
     * @param: [delId]
     * @return: boolean
     * @Date: 2019-07-05
     */
    public boolean deleteUserById(String delId);

    /**
     * @Description:通过条件查询-userList
     * @param: [queryUserName, queryUserRole, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.User>
     * @Date: 2019-07-05
     */
    List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

}
