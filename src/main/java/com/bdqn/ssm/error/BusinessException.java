package com.bdqn.ssm.error;
/**
 * @ClassName: BusinessException
 * @Description:业务异常类(包括业务异常类实现)
 * @Author: amielhs
 * @Date 2019-07-16
 */
public class BusinessException extends Exception implements CommonError{
    private static final long serialVersionUID = 8074155801113466578L;
    //这里的CommonError本质是CommonError的实现类EmBusinessError
    private CommonError commonError;

    public BusinessException() {
    }

    public BusinessException(CommonError commonError) {
        super();//必须需要显示调用super()方法，因为这里没有写无参的构造方法
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError,String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    /**
     * @Description:获取错误的错误代码
     * @param: []
     * @return: int
     * @Date: 2019-07-16
     */
    @Override
    public int getErrorCode() {
        return commonError.getErrorCode();
    }

    /**
     * @Description:获取错误的错误信息
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-16
     */
    @Override
    public String getErrMsg() {
        return commonError.getErrMsg();
    }

    /**
     * @Description:手动设置错误的业务信息(通过定制化的方式处理一些通用的错误信息)
     * @param: [errMsg]
     * @return: com.bdqn.ssm.error.CommonError
     * @Date: 2019-07-16
     */
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
