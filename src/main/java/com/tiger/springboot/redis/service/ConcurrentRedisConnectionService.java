package com.tiger.springboot.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ConcurrentRedisConnectionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    public void concurrentUseRedisTemplate() {
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            redisTemplate.opsForValue().set("", "");
            new Thread(() -> {
                logger.info("start redis thread:{}", ii);
//                    redisTemplate.opsForValue().set("demo" + ii, "demo");
                redisTemplate.execute(new SessionCallback<Boolean>() {
                    @Override
                    public Boolean execute(RedisOperations ops) {
                        //如果有并发问题，循环五次
                        for (int i = 0; i < 5; i++) {
                            ops.multi();
                            BoundZSetOperations operations = ops.boundZSetOps("");
                            operations.removeRange(20, -1);
                            try {
                                TimeUnit.SECONDS.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            List exec = ops.exec();
                            if (exec != null) {
                                return false;
                            }
                        }
                        return true;
                    }
                });

                logger.info("end redis thread:{}", ii);
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void generateBigKey() {
        BoundZSetOperations<String, String> bigKey = redisTemplate.boundZSetOps("bigKey");
        Long generateStartTime = System.currentTimeMillis();

        for (int i = 0; i < 3000000; i++) {
            bigKey.add(i + "", System.currentTimeMillis());
        }
        logger.info("生成大key使用时间:{}", System.currentTimeMillis() - generateStartTime);

//        Long getBigKeyStartTime = System.currentTimeMillis();
//        Set<String> range = bigKey.range(0, -1);
//        logger.info("获取大key 值使用时间:{},size:{}", System.currentTimeMillis() - getBigKeyStartTime,range.size());
//
//        Long delBigKeyStartTime = System.currentTimeMillis();
//        Long aLong = bigKey.removeRange(0, 500000);
//        logger.info("删除大key 值使用时间:{},size:{}", System.currentTimeMillis() - delBigKeyStartTime,aLong);
    }


    public void generateBatchKey() {
        Long generateStartTime = System.currentTimeMillis();
        for (int i = 0; i < 3000000; i++) {
            redisTemplate.opsForValue().set("test"+i,i+"");
        }
        logger.info("生成多个key使用时间:{}", System.currentTimeMillis() - generateStartTime);
    }

}
