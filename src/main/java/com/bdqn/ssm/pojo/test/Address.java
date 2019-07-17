package com.bdqn.ssm.pojo.test;
/**
 * @ClassName: Admin
 * @Description:测试实体类
 * @Author: amielhs
 * @Date 2019-07-17
 */
public class Address {
    private int id;
    private String addName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", addName='" + addName + '\'' +
                '}';
    }
}
