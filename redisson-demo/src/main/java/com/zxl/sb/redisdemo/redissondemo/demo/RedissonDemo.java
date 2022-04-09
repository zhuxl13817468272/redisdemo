package com.zxl.sb.redisdemo.redissondemo.demo;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedissonDemo {
    @Autowired()
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    static final String key = "LOCK_KEY";
    static final String stock = "stock";

    @GetMapping("testAddValue")
    public void testAddValue(){
        stringRedisTemplate.opsForValue().set(stock,"10");
        String s = stringRedisTemplate.opsForValue().get(stock);
        System.out.println("设置成功，值为 = " + s);
    }

    @GetMapping("testRedissonClient")
    public String testRedissonClient() throws InterruptedException {
        //拿到锁对象
        RLock lock = redissonClient.getLock(key);

//        //拿到读写锁对象
//        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
//        // 读锁对象
//        RLock rLock = readWriteLock.readLock();
//        rLock.lock(); // 加锁
//        rLock.unlock(); // 解锁
//        // 写锁对象
//        RLock writeLock = readWriteLock.writeLock();
//        writeLock.lock();// 加锁
//        writeLock.unlock();// 解锁

//        // 获取布隆过滤器对象
//        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(key);
//        // 布隆过滤器初始化参数：预计元素1亿个，误差率为3%
//        bloomFilter.tryInit(100000000L, 0.03);
//        // 使用时：把所有数据存入布隆过滤器
//        bloomFilter.add("zxl");
//        // 使用时：判断布隆过滤器这级缓存中key是否存在
//        System.out.println(bloomFilter.contains("zxl"));
//        System.out.println(bloomFilter.contains("zhuge"));

        lock.lock();

        System.out.println(Thread.currentThread().getName());
        String s = stringRedisTemplate.opsForValue().get(stock);
        Integer integer = Integer.valueOf(s);
        if(integer > 0){
            integer -= 1;
            System.out.println("扣减成功，库存stock = " + integer);
            stringRedisTemplate.opsForValue().set(stock,String.valueOf(integer));
            Thread.sleep(5000);
        }else {
            System.out.println("扣减失败，库存不足");
        }

        lock.unlock();
        return "suceess";
    }
}
