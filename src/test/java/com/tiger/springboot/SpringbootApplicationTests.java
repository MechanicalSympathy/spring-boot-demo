package com.tiger.springboot;

import com.tiger.springboot.redis.service.ConcurrentRedisConnectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

	@Resource
	private ConcurrentRedisConnectionService concurrentRedisConnectionService;
	@Test
	public void testRedisConnection() {
		concurrentRedisConnectionService.concurrentUseRedisTemplate();
	}

	@Test
	public void testBigKey() {
		concurrentRedisConnectionService.generateBigKey();
	}

	@Test
	public void testBatchKey() {
		concurrentRedisConnectionService.generateBatchKey();
	}

}

