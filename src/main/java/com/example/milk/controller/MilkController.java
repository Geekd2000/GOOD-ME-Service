package com.example.milk.controller;

import com.example.milk.entity.Milk;
import com.example.milk.entity.res.ResponseBean;
import com.example.milk.mqtt.msgHandle.MqttGateway;
import com.example.milk.service.MilkService;
import com.example.milk.utils.FtpUtil;
import com.example.milk.utils.RedisUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/milk")
public class MilkController {
    @Autowired
    private MilkService milkService;
    @Resource
    private MqttGateway mqttGateway;

    @GetMapping({"/getRedis"})
    public ResponseBean getRedis(String key) {
        String name = RedisUtils.get(key);
        return ResponseBean.success(name);
    }

    @PostMapping("/mqttSend")
    public ResponseBean sendMqttMsg(String topic, String message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttGateway.sendToMqtt(topic, message);
       return ResponseBean.success("发送消息成功");
    }

    @PostMapping({"/setRedis"})
    public ResponseBean setRedis(String key, String value, @Nullable Integer timeSeconds) {
        if (null == timeSeconds) {
            String s = RedisUtils.set(key, value);
            return ResponseBean.success(s);
        }
        String s = RedisUtils.setex(key, timeSeconds, value);
        return ResponseBean.success(s);
    }

    @GetMapping({"/queryAllMilk"})
    public ResponseBean queryAllMilk() {
        return milkService.queryAllMilk();
    }

    @PostMapping({"/queryById"})
    public ResponseBean queryById(@RequestBody Milk milk) {
        return ResponseBean.success(milkService.queryById(milk));
    }

    @GetMapping({"/queryByName"})
    public ResponseBean queryByName(@RequestParam("mName") String mName) {
        return ResponseBean.success(milkService.queryByName(mName));
    }

    @ApiOperation(notes = "/uploadImg", value = "上传图片至FTP")
    @PostMapping("/uploadImg")
    public ResponseBean uploadImg(File file) throws Exception {
        //初始化FTP 设置用户名密码
        FtpUtil ftpUtil = getInitedFtp();
        //根据当前时间戳与登录的营销人员ID生成文件名
        String fileName = System.currentTimeMillis() + "&" + "jjy" + ".jpg";
        boolean b = ftpUtil.uploadFile(path, fileName, new FileInputStream(file));
        if (!b) {
            return ResponseBean.fail("1111", "上传失败！");//上传失败,请重新上传...
        }
        return ResponseBean.success(Collections.singletonMap("url", url + fileName));
    }

    @ApiOperation(notes = "/delFile", value = "删除FTP文件")
    @PostMapping("/delFile")
    public ResponseBean delFile(String fileName) throws Exception {
        //初始化FTP 设置用户名密码
        FtpUtil ftpUtil = getInitedFtp();
        //根据当前时间戳与登录的营销人员ID生成文件名
        boolean b = ftpUtil.deleteFile(path, fileName);
        if (!b) {
            return ResponseBean.fail("2222", "删除文件失败！");
        }
        return ResponseBean.success("删除成功!");
    }

    @Value("${testftp.host}")
    private String host;

    @Value("${testftp.port}")
    private Integer port;

    @Value("${testftp.username}")
    private String username;

    @Value("${testftp.psword}")
    private String psword;

    @Value("${testftp.path}")
    private String path;

    @Value("${testftp.url}")
    private String url;

    //设置FTP地址帐号密码、初始化FTP
    private FtpUtil getInitedFtp() {
        FtpUtil ftpUtil = new FtpUtil(host, port, username, psword);
        ftpUtil.initFtpClient();
        return ftpUtil;
    }

}