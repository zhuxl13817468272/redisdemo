package com.zxl.sb.redisdemo.redissondemo.msg.msgConfig;

import com.zxl.sb.redisdemo.redissondemo.msg.receiver.MyMessageListener;
import com.zxl.sb.redisdemo.redissondemo.msg.receiver.MyMessageListener2;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 配置监听适配器、消息监听容器
 * @program: redisdemo
 * @description
 * @author: Zhu Xiaolong
 * @create: 2021-10-25 19:46
 **/
@Configuration
@EnableCaching
public class RedisCacheConfig {
    @Bean
    RedisConnectionFactory connectionFactory(){
        return new LettuceConnectionFactory();
    }
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机
        container.addMessageListener(new MyMessageListener(), new PatternTopic("channel:test"));
        container.addMessageListener(new MyMessageListener2(), new PatternTopic("channel:test2"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MyMessageListener receiver) {
        System.out.println("消息适配器1");
        return new MessageListenerAdapter(receiver, "onMessage");
    }

    @Bean
    MessageListenerAdapter listenerAdapter2(MyMessageListener2 receiver) {
        System.out.println("消息适配器2");
        return new MessageListenerAdapter(receiver, "onMessage");
    }


    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


}
