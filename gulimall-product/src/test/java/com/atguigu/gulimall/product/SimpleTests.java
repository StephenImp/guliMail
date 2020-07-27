package com.atguigu.gulimall.product;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 类说明： <br/>
 * author:huaguoguo
 * Date: 2020/5/17
 * Time: 15:03
 */
public class SimpleTests {

    @Test
    public void test1() {
        List<String> list = new ArrayList();
        long count = 0l;
        while (true) {
            System.out.println("循环次数：" + count++);
            list.add(UUID.randomUUID().toString());
        }
    }
}
