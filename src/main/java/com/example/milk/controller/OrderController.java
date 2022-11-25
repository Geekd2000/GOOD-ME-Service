package com.example.milk.controller;

import com.example.milk.entity.Order;
import com.example.milk.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public OrderController() {
    }

    @PostMapping({"/insertOrder"})
    public boolean insertOrder(@RequestBody Order order) {
        return this.orderService.insertOrder(order);
    }

    @GetMapping({"/queryByOrderNo"})
    public Order queryByOrderNo(@RequestParam("oNo") String oNo) {
        return this.orderService.queryByOrderNo(oNo);
    }

    @GetMapping({"/queryAllOrderByUid"})
    public List<Order> queryAllOrderByUid(@RequestParam("uid") int uid) {
        return this.orderService.queryAllOrderByUid(uid);
    }
}
