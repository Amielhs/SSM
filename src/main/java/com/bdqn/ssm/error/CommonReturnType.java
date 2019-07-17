package com.bdqn.ssm.error;
/**
 * @ClassName: CommonReturnType
 * @Description:通用的返回类型
 * @Author: amielhs
 * @Date 2019-07-16
 */
public class CommonReturnType {

    //表明对应请求的处理结果为success和fail
    private String status;

    //若status=success，则data内返回前端需要的JSON数据
    //若status=fail，则data内返回通用的错误码格式(已在其他类或接口中定义)
    private Object data;

    public CommonReturnType() {
    }

    /**
     * @Description:构造通用返回数据类型
     * @param: [result, status]
     * @return: com.bdqn.ssm.error.CommonReturnType
     * @Date: 2019-07-16
     */
    public static CommonReturnType create(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return  commonReturnType;
    }

    /**
     * @Description:如果返回的结果不带status，则返回的status为success
     * @param: [result]
     * @return: com.bdqn.ssm.error.CommonReturnType
     * @Date: 2019-07-16
     */
    public static CommonReturnType create(Object result){
        return  CommonReturnType.create(result,"success");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
