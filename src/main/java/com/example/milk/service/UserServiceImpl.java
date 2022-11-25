package com.example.milk.service;

import com.alibaba.fastjson.JSONObject;
import com.example.milk.dao.UserMapper;
import com.example.milk.entity.User;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl() {
    }

    public boolean userRegister(User user) {
        boolean flag = false;

        try {
            this.userMapper.userRegister(user);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }

    public User queryByUsername(String username) {
        User user = userMapper.queryByUsername(username);
        log.error("=========>user:{}", JSONObject.toJSON(user));
        return user;
    }

    public List<User> queryAllUser() {
        return this.userMapper.queryAllUser();
    }

    public boolean userDelete(String username) {
        boolean flag = false;

        try {
            this.userMapper.userDelete(username);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }

    public boolean updatePassword(User user) {
        boolean flag = false;

        try {
            this.userMapper.updatePassword(user);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }

    public boolean updatePersonalInfo(User user) {
        boolean flag = false;

        try {
            this.userMapper.updatePersonalInfo(user);
            flag = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return flag;
    }
}
