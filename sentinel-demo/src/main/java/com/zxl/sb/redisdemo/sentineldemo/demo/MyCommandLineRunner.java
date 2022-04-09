package com.zxl.sb.redisdemo.sentineldemo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner, Ordered {
    @Autowired
    private IndexController indexController;

    @Override
    public void run(String... args) throws Exception {
        indexController.testSentinel();
    }

    @Override
    public int getOrder() {
        return 1; // 返回执行顺序
    }
}
