package com.bdqn.ssm.error;
/**
 * @ClassName: EmBusinessError
 * @Description:业务错误的枚举(需要实现CommonError中的方法，即CommonError的实现枚举)
 * @Author: amielhs
 * @Date 2019-07-16
 */
public enum EmBusinessError implements CommonError{
    //通用的错误类型10000开头
    UNKNOWN_ERROR(10002,"未知错误"),
    //用户的错误类型20000开头
    UNFINDUSER_ERROR(20001,"未找到用户信息")
    ;

    private int errCode;//错误代码
    private String errMsg;//错误信息

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * @Description:获取错误的错误代码
     * @param: []
     * @return: int
     * @Date: 2019-07-16
     */
    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    /**
     * @Description:获取错误的错误信息
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-16
     */
    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    /**
     * @Description:手动设置错误的业务信息(通过定制化的方式处理一些通用的错误信息)
     * @param: [errMsg]
     * @return: com.bdqn.ssm.error.CommonError
     * @Date: 2019-07-16
     */
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
