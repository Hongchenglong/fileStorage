package com.oeong.controller;

import com.oeong.entity.User;
import com.oeong.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    // 登录方法
    @PostMapping("/login")
    public String login(User user, Model model) {
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("user", (User) subject.getPrincipal());
            System.out.println("登录成功");
            return "redirect:/file/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名或密码错误");
            return "user/login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "用户名或密码错误");
            return "user/login";
        }
    }

}
