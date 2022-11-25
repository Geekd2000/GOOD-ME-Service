package com.example.milk.service;

import com.example.milk.entity.User;
import java.util.List;

public interface UserService {
    boolean userRegister(User user);

    User queryByUsername(String username);

    List<User> queryAllUser();

    boolean userDelete(String username);

    boolean updatePassword(User user);

    boolean updatePersonalInfo(User user);
}
