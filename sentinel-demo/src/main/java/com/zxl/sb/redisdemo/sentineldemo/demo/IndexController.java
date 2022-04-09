package com.zxl.sb.redisdemo.sentineldemo.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IndexController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 测试节点挂了哨兵重新选举新的master节点，客户端是否能动态感知到
     * 新的master选举出来后，哨兵会把消息发布出去，客户端实际上是实现了一个消息监听机制，
     * 当哨兵把新master的消息发布出去，客户端会立马感知到新master的信息，从而动态切换访问的masterip
     *
     * sentinel重新选主时间大概有30多秒，期间会出现连接中断，会有数据丢失，需要从日志中进行人工补偿
     *
     * 先对比集群来说，有两个致命问题：
     *  1.sentinel选主时，会有几秒至几十秒的瞬时连接中断（集群中虽然也有选主瞬时中断，但是其他节点可以相应其他相应key），会有数据丢失。
     *  2.sentinel模式，只有一个主redis对外提供服务（读写），并发量没有集群高且后期没法自动扩展。
     *
     * @throws InterruptedException
     */
    @RequestMapping("/test_sentinel")
    public void testSentinel() throws InterruptedException {
        int i = 100;  //132
        while (true){
            try {
                stringRedisTemplate.opsForValue().set("zxl"+i, i+"");
                System.out.println("设置key："+ "zxl" + i);
                i++;
                Thread.sleep(1000);
            }catch (Exception e){
                log.error("错误：", e);
            }
        }
    }


}

