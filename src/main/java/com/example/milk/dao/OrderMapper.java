package com.example.milk.dao;

import com.example.milk.entity.Order;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

    @Insert("insert into `order`(u_id, m_id, o_time, o_no, o_price, o_type, o_count) " +
            " values (#{uid}, #{mid}, #{oTime}, #{oNo}, #{oPrice}, #{oType}, #{oCount})")
    boolean insertOrder(Order order);

    @Select("select * from `order` where o_no = #{oNo}")
    Order queryByOrderNo(String oNo);

    @Select("select * from `order` where u_id = #{uid}")
    List<Order> queryAllOrderByUid(int uid);
}
