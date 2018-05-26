package com.treader.demo.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void setToCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void appendToCache(String key, String appendValue) {
        redisTemplate.opsForValue().append(key, appendValue);
    }

    public String getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getFromCache(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    public void setToCacheTTL(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public void rpush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public List<String> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void delKey(String key) {
        redisTemplate.delete(key);
    }
}
