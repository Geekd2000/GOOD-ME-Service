package com.example.milk.task;

import com.alibaba.fastjson.JSONObject;
import com.example.milk.entity.res.ResponseBean;
import com.example.milk.service.MilkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @ClassName: SaticScheduleTask
 * @Description: 定时任务
 * @date: 2022/03/12
 * @author: Jiangjunye
 * @version: v1.0.0
 */
@Configuration    //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling //2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    private MilkService milkService;

    //3.添加定时任务
//    @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
//        ResponseBean responseBean = milkService.queryAllMilk();
//        System.out.println(JSONObject.toJSON(responseBean.getData()));
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

}
