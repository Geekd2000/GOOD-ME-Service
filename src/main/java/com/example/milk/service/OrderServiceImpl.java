package com.example.milk.service;

import com.example.milk.dao.OrderMapper;
import com.example.milk.entity.Order;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public OrderServiceImpl() {
    }

    public boolean insertOrder(Order order) {
        return this.orderMapper.insertOrder(order);
    }

    public Order queryByOrderNo(String oNo) {
        return this.orderMapper.queryByOrderNo(oNo);
    }

    public List<Order> queryAllOrderByUid(int uid) {
        return this.orderMapper.queryAllOrderByUid(uid);
    }
}
