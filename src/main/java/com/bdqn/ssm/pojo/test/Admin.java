package com.bdqn.ssm.pojo.test;
/**
 * @ClassName: Admin
 * @Description:测试实体类
 * @Author: amielhs
 * @Date 2019-07-17
 */
public class Admin {
    private String name;
    private int age;

    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}
