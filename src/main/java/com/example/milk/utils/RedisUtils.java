package com.example.milk.utils;

import com.example.milk.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @ClassName: RedisUtils
 * @Description: Redis工具类
 * @date: 2022/01/20
 * @author: Jiangjunye
 * @version: v1.0.0
 */
@Slf4j
@Component
public class RedisUtils {

    private static JedisPool jedisPool;

    @Autowired
    private RedisConfig redisConfig;

    /**
     * JedisPool 无法通过@Autowired注入，可能由于是方法bean的原因，此处可以先注入RedisConfig，
     * 然后通过@PostConstruct初始化的时候将factory直接赋给jedisPool
     */
    @PostConstruct
    public void init() {
        jedisPool = redisConfig.redisPoolFactory();
    }

    public static String get(String key) {
        Jedis jedis = null;
        String value = null;

        try {
            jedis = jedisPool.getResource();//获取一个jedis实例
            jedis.select(0);//默认选择第0个数据库
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("错误日志：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 存储数据
     * @param key     键
     * @param seconds 过期时间-秒
     * @param value   值
     * @return
     */
    public static String setex(String key, Integer seconds, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();//获取一个jedis实例
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.error("错误日志：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * 存储数据
     * @param key   键
     * @param value 值
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();//获取一个jedis实例
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("错误日志：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return result;
    }

}
