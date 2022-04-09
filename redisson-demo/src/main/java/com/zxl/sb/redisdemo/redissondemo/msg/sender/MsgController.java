package com.zxl.sb.redisdemo.redissondemo.msg.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: redisdemo
 * @description
 * @author: Zhu Xiaolong
 * @create: 2021-10-25 19:57
 **/
@RestController
public class MsgController {
    @Autowired
    StringRedisTemplate template;

    /**
     * 发布消息
     *
     * @param id
     * @return
     */
    @RequestMapping("/sendMessage/{id}")
    public String sendMessage(@PathVariable String id) {
        for(int i = 1; i <= 5; i++) {
            template.convertAndSend("channel:test", String.format("我是消息{%d}号: %tT", i, new Date()));
        }
        return "";
    }


    /**
     * 发布消息
     *
     * @param id
     * @return
     */
    @RequestMapping("/sendMessage2/{id}")
    public String sendMessage2(@PathVariable String id) {
        for(int i = 1; i <= 5; i++) {
            template.convertAndSend("channel:test2", String.format("我是消息{%d}号: %tT", i, new Date()));
        }
        return "";
    }

}
