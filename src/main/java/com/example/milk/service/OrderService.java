package com.example.milk.service;

import com.example.milk.entity.Order;
import java.util.List;

public interface OrderService {
    boolean insertOrder(Order order);

    Order queryByOrderNo(String oNo);

    List<Order> queryAllOrderByUid(int uid);
}
