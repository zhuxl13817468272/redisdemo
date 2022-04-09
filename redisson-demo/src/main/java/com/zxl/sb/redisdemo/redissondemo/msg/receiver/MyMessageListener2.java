package com.zxl.sb.redisdemo.redissondemo.msg.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 参考文章： https://www.cnblogs.com/powerwu/p/11505481.html
 * @program: redisdemo
 * @description  继承MessageListener，就能拿到消息体和频道名
 * @author: Zhu Xiaolong
 * @create: 2021-10-25 17:19
 **/
@Slf4j
@Component
public class MyMessageListener2 implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {

        String channel = new String(message.getChannel());
        String msgBody = new String(message.getBody());

        String msg = message.toString();
        log.info("22---消息channel: {},消息body: {}",channel,msgBody);
        log.info("22---整个消息: {}",msg);
    }


}
