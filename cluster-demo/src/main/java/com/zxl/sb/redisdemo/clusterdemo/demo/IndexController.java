package com.zxl.sb.redisdemo.clusterdemo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/test_cluster")
    public void testCluster() throws InterruptedException {
        stringRedisTemplate.opsForValue().set("zxlgeniuscluster","666-888");
        stringRedisTemplate.opsForValue().set("rediscluster","666-888-999");
        System.out.println(stringRedisTemplate.opsForValue().get("zxlgeniuscluster"));
        System.out.println(stringRedisTemplate.opsForValue().get("rediscluster"));
    }
}
