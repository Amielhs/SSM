package com.bdqn.ssm.utils;

import com.bdqn.ssm.pojo.User;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ClassName: SysInterceptor
 * @Description:自定义拦截器
 * @Author: amielhs
 * @Date 2019-07-15
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(SysInterceptor.class);

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        logger.debug("SysInterceptor preHandle!");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user==null){
            response.sendRedirect(request.getContextPath()+"/401.jsp");
            return false;
        }
        return true;
    }
}
