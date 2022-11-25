package com.example.milk.service;

import com.example.milk.entity.Milk;
import com.example.milk.entity.res.ResponseBean;

import java.util.List;

public interface MilkService {
    ResponseBean queryAllMilk();

    Milk queryById(Milk milk);

    Milk queryByName(String mName);
}
