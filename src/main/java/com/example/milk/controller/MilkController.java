package com.example.milk.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.milk.entity.Milk;
import com.example.milk.entity.res.ResponseBean;
import com.example.milk.service.MilkService;
import com.example.milk.utils.FtpUtil;
import com.example.milk.utils.RedisUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/milk")
public class MilkController {
    @Autowired
    private MilkService milkService;

    @GetMapping({"/getRedis"})
    public ResponseBean getRedis() {
        String name = RedisUtils.get("name");
        return ResponseBean.success(name);
    }

    @PostMapping({"/setRedis"})
    public ResponseBean setRedis() throws Exception {
        String s = RedisUtils.set("name", 30, "JiangJunYe");
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        OkHttpClient httpClient = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

        String post = "{\"test\":123}";
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, post);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        return ResponseBean.success(s);
    }

    @GetMapping({"/get"})
    public ResponseBean get(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx679b94a2d1e86cad" +
                "&secret=4c28d35d307acbf9279be0020c043151&js_code=" + code + "&grant_type=authorization_code";
        OkHttpClient httpClient = new OkHttpClient();
        log.error("------->>>请求url:{}", url);
        String token = Md5Crypt.md5Crypt("11111111111111111111".getBytes());
        log.error("-------------------------token:{}", token);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
//        Object parse = JSON.parse(response.body().string());
        Map map = JSONObject.parseObject(response.body().string(), Map.class);
        log.error("------->>>{}", JSONObject.toJSONString(map));
        return ResponseBean.success(map);
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