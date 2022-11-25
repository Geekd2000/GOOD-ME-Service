package com.example.milk.controller;

import com.example.milk.entity.User;
import com.example.milk.entity.res.ResponseBean;
import com.example.milk.service.UserService;
import com.example.milk.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController() {
    }

    @PostMapping({"/register"})
    public String userRegister(@RequestBody User user) {
        User ur = this.userService.queryByUsername(user.getUsername());
        if (ur != null) {
            return "用户已存在";
        } else {
            return this.userService.userRegister(user) ? "注册成功" : "注册失败";
        }
    }

    @GetMapping({"/login"})
    public String userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = this.userService.queryByUsername(username);
        String result = "";
        if (user == null) {
            result = "用户名不存在";
        } else if (!user.getPassword().equals(password)) {
            result = "密码错误";
        } else {
            result = "登录成功";
        }

        return result;
    }

    /**
     * JWT登录
     */
    private static final String USERNAME="admin";
    private static final String PASSWORD="123456";
    @GetMapping({"/logintest"})
    public ResponseBean login(User user) {
        if (USERNAME.equals(user.getUsername()) && PASSWORD.equals(user.getPassword())){
            //添加token
            user.setToken(JwtUtil.createToken());
            return ResponseBean.success(user);
        }
        return ResponseBean.fail("11111");
    }

    @GetMapping({"/queryByUsername"})
    public User queryByUsername(@RequestParam("username") String username) {
        return this.userService.queryByUsername(username);
    }

    @GetMapping({"/queryAllUser"})
    public List<User> queryAllUser() {
        return this.userService.queryAllUser();
    }

    @PostMapping({"/userDelete"})
    public Boolean userDelete(@RequestParam("username") String username) {
        User user = this.userService.queryByUsername(username);
        return user == null ? false : this.userService.userDelete(username);
    }

    @PostMapping({"/updatePassword"})
    public String updatePassword(@RequestBody User user) {
        User judge = this.userService.queryByUsername(user.getUsername());
        if (judge != null) {
            return this.userService.updatePassword(user) ? "修改成功" : "修改失败";
        } else {
            return "用户名不存在";
        }
    }

    @PostMapping({"/updatePersonalInfo"})
    public String updatePersonalInfo(@RequestBody User user) {
        User judge = this.userService.queryByUsername(user.getUsername());
        if (judge != null) {
            return this.userService.updatePersonalInfo(user) ? "修改成功" : "修改失败";
        } else {
            return "用户名不存在";
        }
    }
}
