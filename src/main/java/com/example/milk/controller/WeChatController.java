package com.example.milk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.milk.entity.res.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @GetMapping("/getJsapiTicket")
    public ResponseBean getJsapiTicket(@RequestParam String accessToken) throws Exception {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + accessToken;
        OkHttpClient httpClient = new OkHttpClient();
        log.info("------->>>请求url:{}", url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
//        Object parse = JSON.parse(response.body().string());
        Map map = JSONObject.parseObject(response.body().string(), Map.class);
        log.info("------->>>{}", JSONObject.toJSONString(map));
        return ResponseBean.success(map);
    }

    @GetMapping("/getSignature")
    public ResponseBean getSignature(@RequestParam String ticket, @RequestParam String url) throws Exception {
        log.info("------->>>入参ticket:{}", ticket);
        if (Objects.isNull(ticket) || Objects.isNull(url)) {
            return ResponseBean.fail("410", "ticket和url参数不能为空!");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = generateRandomStr(10);
        /**
         * jsapi_ticket=JSAPITICKET&noncestr=NONCESTR&timestamp=TIMESTAMP&url=URL
         */
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=").append(ticket).append("&")
                .append("noncestr=").append(nonceStr).append("&")
                .append("timestamp=").append(timestamp).append("&")
                .append("url=").append(url);
        log.error("singer str:{}", sb);
        String signature = sha1(sb.toString());
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("timestamp", timestamp);
        resMap.put("nonceStr", nonceStr);
        resMap.put("signature", signature);
        log.info("------->>>resMap:{}", JSON.toJSONString(resMap));
        return ResponseBean.success(resMap);
    }

    /**
     * SHA1签名
     *
     * @param s
     * @return
     */
    public static String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA1签名异常", e);
        }
        return "";
    }

    /**
     * 生成随机字符（包含数字和字母）
     *
     * @param length
     * @return
     */
    public static String generateRandomStr(int length) {
        UUID uuid = UUID.randomUUID();
        String val = uuid.toString().replaceAll("-", "");
        String ret = val.substring(0, length);
        return ret;
    }

}