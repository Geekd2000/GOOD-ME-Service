package com.example.milk.dao;

import com.example.milk.entity.Milk;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MilkMapper {

    @Select("select * from milk")
    List<Milk> queryAllMilk();

    @Select("select * from milk where m_id = #{mid}")
    Milk queryById(int mid);

    @Select("select * from milk where m_name = #{mName}")
    Milk queryByName(@Param("mName") String mName);
}
