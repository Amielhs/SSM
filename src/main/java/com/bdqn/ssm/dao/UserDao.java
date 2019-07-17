package com.bdqn.ssm.dao;

import com.bdqn.ssm.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UserDao
 * @Description:用户数据访问层
 * @Author: amielhs
 * @Date 2019-07-04
 */
public interface UserDao {
    /**
     * @Description:通过uId查找用户
     * @param: [uId]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-04
     */
    User selectUserById(@Param("uId") String uId);

    /**
     * @Description:通过userCode和userPassword登录
     * @param: [userCode, userPassword]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-04
     */
    public User getLoginUser(@Param("uCode") String userCode,@Param("pwd")String userPassword);

    /**
     * @Description:通过userCode获取User
     * @param: [userCode]
     * @return: com.bdqn.ssm.pojo.User
     * @Date: 2019-07-05
     */
    public User getLoginUserOnly(@Param("uCode") String userCode);

    /**
     * @Description:通过条件查询-用户表记录数
     * @param: [userName, userRole]
     * @return: int
     * @Date: 2019-07-05
     */
    public Integer getUserCount(@Param("uName") String userName,@Param("uRole") int userRole);

    /**
     * @Description:增加用户信息
     * @param: [user]
     * @return: int
     * @Date: 2019-07-05
     */
    public int add(User user);

    /**
     * @Description:修改用户信息
     * @param: [user]
     * @return: int
     * @Date: 2019-07-05
     */
    public int modify(User user);

    /**
     * @Description:修改当前用户密码
     * @param: [id, pwd]
     * @return: int
     * @Date: 2019-07-05
     */
    public int updatePwd(@Param("id") int id,@Param("pwd") String pwd);

    /**
     * @Description:通过userId删除user
     * @param: [delId]
     * @return: int
     * @Date: 2019-07-05
     */
    public int deleteUserById(@Param("delId") String delId);

    /**
     * @Description:通过条件查询-userList
     * @param: [userName, userRole, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.User>
     * @Date: 2019-07-05
     */
    public List<User> getUserList(@Param("uName") String userName,@Param("uRole")int userRole,@Param("from") int currentPageNo,@Param("pSize") int pageSize);


}
