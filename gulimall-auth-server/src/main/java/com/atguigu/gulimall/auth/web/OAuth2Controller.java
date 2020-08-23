package com.atguigu.gulimall.auth.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.ums.AuthServerConstant;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.vo.SocialUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 处理社交登录
 * @Author 鲁班爱喝旺仔
 * @Date 2020/4/24 17:17
 * @Version 1.0
 **/
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;

    /**
     * 根据code换取accessToken - 调用远程gulimall-member服务进行社交登录或注册
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {

        // 根据code换取accessToken
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "908582850");
        map.put("client_secret", "6226758e6e6d623d1bceccefccf22a0d");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<String, String>(), null, map);

        // 2、处理响应数据
        if (response.getStatusLine().getStatusCode() == 200) {
            // 获取到了accessToken
            String json = EntityUtils.toString(response.getEntity()); // 将响应的内容转换成json字符串
            SocialUserVo socialUserVo = JSON.parseObject(json, SocialUserVo.class); // 将获取到的json转换成SocialUserVo对象

            // 1、会员第一次登陆，自动注册（为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员信息）
            // 调用远程服务 gulimall-member 登陆或注册操作
            R r = memberFeignService.oauthLogin(socialUserVo);
            if (r.getCode() == 0) {
                MemberRespVo memberRespVo = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("微博用户登录成功，用户信息：{}", memberRespVo);
                //第一次使用session:命令浏览器保存jsessionID放在cookie里
                //以后浏览器访问哪个网站，就会带上这个网站的cookie
                //子域之间:  gulimail.com(父域名)   auth.gulimail.com(子域名)  order.gulimail.com(子域名)
                //在服务器给浏览器发送jsessionID的时候，即使是子域名系统发的jsessionID，父域系统也能使用
                //就是如果是子域名系统发JsessionID的时候,指定域名为父域名
                //new Cookie("JSESSIONID","test").setDomain("xxx");
                //servletResponse.addCookie();

                session.setAttribute(AuthServerConstant.LOGIN_USER, memberRespVo);

                return "redirect:http://gulimall.com";
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }

        } else { // 远程请求社交服务获取数据失败
            return "redirect:http://auth.gulimall.com/login.html";
        }



    }
}
