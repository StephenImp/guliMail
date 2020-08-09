package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author 鲁班爱喝旺仔
 * @Date 2020/4/11 18:27
 * @Version 1.0
 **/
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    /* 首页面跳转 */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {

        List<CategoryEntity> categoryEntityList = categoryService.getLeve1Categorys();

        model.addAttribute("categorys", categoryEntityList);

        return "index";
    }

//    index/json/catalog.json
    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        RLock mylock = redisson.getLock("mylock");

        // 锁的自动续期,如果业务超长，运行期间自动给锁续上新的30s，不用担心业务时间超长，锁自动过期被删掉
        // 加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s以后自动删除
        mylock.lock(); // 加锁 阻塞式等待,默认是30s


        try {
            System.out.println(Thread.currentThread().getId() + "---> 加锁成功");
            Thread.sleep(10000);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mylock.unlock(); // 解锁
            System.out.println(Thread.currentThread().getId() + " ---> 释放锁");
        }

        return "hello";
    }

}

