package com.bdqn.ssm.controller;

import com.bdqn.ssm.error.CommonReturnType;
import com.bdqn.ssm.pojo.test.Admin;
import com.bdqn.ssm.pojo.test.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: TestController
 * @Description:测试控制器
 * @Author: amielhs
 * @Date 2019-07-17
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    /**
     * @Description:基本数据的绑定
     * @param: [age]
     * @return: java.lang.Object
     * @Date: 2019-07-17
     */
    @RequestMapping(value = "/baseType")
    @ResponseBody
    public Object baseType(int age){
        /*return "age:"+age;*/
        return CommonReturnType.create("age:"+age);
    }

    /**
     * @Description:包装类的绑定
     * @param: [age]
     * @return: java.lang.Object
     * @Date: 2019-07-17
     */
    @RequestMapping(value = "/packageType")
    @ResponseBody
    public Object packageType(Integer age){
        return CommonReturnType.create("age:"+age);
    }

    /**
     * @Description:数组的绑定
     * @param: [games]
     * @return: java.lang.Object
     * @Date: 2019-07-17
     */
    @RequestMapping(value = "/arrayType")
    @ResponseBody
    public Object arrayType(String[] games){
        StringBuilder stringBuilder = new StringBuilder();
        for (String game:
             games) {
            stringBuilder.append(game).append("---");
        }
        return CommonReturnType.create("games的长度:"+games.length+"--内容："+stringBuilder);
    }

    /**
     * @Description:对象的绑定
     * @param: [admin]
     * @return: java.lang.Object
     * @Date: 2019-07-17
     */
    @RequestMapping(value = "/objectType")
    @ResponseBody
    public Object objectType(Admin admin, Student student){
        return CommonReturnType.create("admin:"+admin.toString()+"----- student:"+student.toString());
    }

    @InitBinder(value = "admin")
    public void initAdmin(WebDataBinder binder){
        binder.setFieldDefaultPrefix("admin.");
    }

    @InitBinder(value = "student")
    public void initStudent(WebDataBinder binder){
        binder.setFieldDefaultPrefix("student.");
    }

    /**
     * @Description:布尔类型的绑定
     * @param: []
     * @return: java.lang.Object
     * @Date: 2019-07-17
     */
    @RequestMapping(value = "/booleanType")
    @ResponseBody
    public Object booleanType(Boolean boo){
        return CommonReturnType.create("boolean:"+boo.toString());
    }
}
