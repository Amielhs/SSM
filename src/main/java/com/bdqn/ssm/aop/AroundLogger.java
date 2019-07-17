package com.bdqn.ssm.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;


/**
 * @ClassName: ServiceLogger
 * @Description:定义包含增强方法的JavaBean(实现环绕增强)
 * @Author: amielhs
 * @Date 2019-06-26
 */
@Aspect //使用注解定义切面
public class AroundLogger {
    private static final Logger log = Logger.getLogger(AroundLogger.class);

    //通过注解定义切点组件(业务层所有的方法)
    @Pointcut(value = "execution(* com.bdqn.ssm.service..*.*(..))")
    public void pointCut(){}

    @Around(value = "pointCut()")
    public Object aroundLogger(ProceedingJoinPoint jp) throws Throwable {
        //前置增强
        log.info("调用 " + jp.getTarget() + " 的 " + jp.getSignature().getName()
                + " 方法。方法入参：" + Arrays.toString(jp.getArgs()));
        try {
            Object result = jp.proceed();
            //后置增强
            log.info("调用 " + jp.getTarget() + " 的 " + jp.getSignature().getName() + " 方法。方法返回值：" + result);
            return result;
        } catch (Throwable e) {
            //异常抛出增强
            log.error(jp.getSignature().getName() + " 方法发生异常：" + e);
            throw e;
        }finally {
            //最终增强
            log.info(jp.getSignature().getName() + " 方法结束执行。");
        }
    }
}
