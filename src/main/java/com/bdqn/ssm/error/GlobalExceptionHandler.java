package com.bdqn.ssm.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description:全局异常捕获类(所有的异常，业务异常，其他异常)
 * @Author: amielhs
 * @Date 2019-07-16
 */
@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request,Exception e) throws Exception{
        Map<String,Object> resposeData = new HashMap<String,Object>();
        if (e instanceof BusinessException){//业务异常
            BusinessException businessException = (BusinessException) e;
            resposeData.put("errCode",businessException.getErrorCode());
            resposeData.put("errMsg",businessException.getErrMsg());

        }else {//否则返回的是未知异常的信息(代码+文字)
            resposeData.put("errCode",EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            resposeData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        //最后，无论是业务异常还是未知异常都将其存储到格式化后的异常类中(并且都是异常，即处理失败)
        return CommonReturnType.create(resposeData,"fail");
    }
}
