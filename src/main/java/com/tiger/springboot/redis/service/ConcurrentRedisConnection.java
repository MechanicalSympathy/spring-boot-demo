package com.tiger.springboot.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConcurrentRedisConnection {
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    public void concurrentUseRedisTemplate() {
        redisTemplate.opsForValue().set("demo", "demo");
    }

}
