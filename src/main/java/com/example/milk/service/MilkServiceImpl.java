package com.example.milk.service;

import com.alibaba.fastjson.JSONObject;
import com.example.milk.dao.MilkMapper;
import com.example.milk.entity.Milk;

import java.util.List;

import com.example.milk.entity.res.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MilkServiceImpl implements MilkService {
    @Autowired
    private MilkMapper milkMapper;

    public MilkServiceImpl() {
    }

    public ResponseBean queryAllMilk() {
        List<Milk> milkList = milkMapper.queryAllMilk();
        log.error("milkList:{}", JSONObject.toJSON(milkList));
        return ResponseBean.success(milkList);
    }

    public Milk queryById(Milk milk) {
        return this.milkMapper.queryById(milk.getMid());
    }

    public Milk queryByName(String mName) {
        return this.milkMapper.queryByName(mName);
    }
}
