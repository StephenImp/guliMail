package com.atguigu.gulimall.search.pojo;

import lombok.Data;

/**
 * 描述： es测试数据中的数据对象封装类 <br>
 * CreateDate: 2020/5/29 <br>
 *
 * @author huaguoguo
 */
@Data
public class Account {


    private int accountNumber;
    private double balance;
    private String firstname;
    private String lastname;
    private int age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;
}
