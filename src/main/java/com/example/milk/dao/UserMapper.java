package com.example.milk.dao;

import com.example.milk.entity.User;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Insert("insert into user(username, password, nickname, phone) " +
            "  values (#{username}, #{password}, #{username}, #{phone})")
    void userRegister(User user);

    @Select("select * from user where username = #{username}")
    User queryByUsername(String username);

    @Select("select * from user")
    List<User> queryAllUser();

    @Delete("delete from user where username = #{username}")
    void userDelete(String username);

    @Update("update user set password=#{password} where username = #{username}")
    void updatePassword(User user);

    @Update("update user set nickname=#{nickname}, phone=#{phone} " +
            " where username = #{username}")
    void updatePersonalInfo(User user);
}
